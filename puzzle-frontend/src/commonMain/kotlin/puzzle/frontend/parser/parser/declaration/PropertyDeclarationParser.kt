package puzzle.frontend.parser.parser.declaration

import puzzle.ast.declaration.*
import puzzle.ast.expression.Identifier
import puzzle.ast.parameter.ParameterReference
import puzzle.ast.type.LambdaType
import puzzle.ast.type.NamedType
import puzzle.ast.type.TypeReference
import puzzle.ast.type.copy
import puzzle.context.FileContext
import puzzle.base.location.SourceLocation
import puzzle.base.location.copy
import puzzle.base.location.span
import puzzle.frontend.lexer.syntaxError
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.frontend.parser.isAnonymousBinding
import puzzle.frontend.parser.parser.ModifierTarget
import puzzle.frontend.parser.parser.check
import puzzle.frontend.parser.parser.expression.IdentifierTarget
import puzzle.frontend.parser.parser.expression.parseExpressionChain
import puzzle.frontend.parser.parser.expression.parseIdentifier
import puzzle.frontend.parser.parser.parseModifiers
import puzzle.frontend.parser.parser.statement.parseStatement
import puzzle.frontend.parser.parser.statement.parseStatements
import puzzle.frontend.parser.parser.type.parseTypeReference
import puzzle.frontend.parser.syntaxError
import puzzle.token.kinds.AccessKind.DOT
import puzzle.token.kinds.AccessKind.QUESTION_DOT
import puzzle.token.kinds.AccessorKind.GET
import puzzle.token.kinds.AccessorKind.SET
import puzzle.token.kinds.AssignmentKind.ASSIGN
import puzzle.token.kinds.BracketKind.End.RBRACKET
import puzzle.token.kinds.BracketKind.End.RPAREN
import puzzle.token.kinds.BracketKind.Start.*
import puzzle.token.kinds.ModifierKind.*
import puzzle.token.kinds.OperatorKind.LT
import puzzle.token.kinds.SeparatorKind.COMMA
import puzzle.token.kinds.SymbolKind.COLON
import puzzle.token.kinds.isIn

context(_: FileContext, cursor: PzlTokenCursor)
fun parsePropertyDeclaration(meta: DeclarationMeta, start: SourceLocation, isTopLevel: Boolean): PropertyDeclaration {
	var funExtension: TypeReference? = null
	val isMutable = cursor.previous.kind == VAR
	val propertySpec = if (cursor.match(LBRACKET)) {
		parseDestructurePropertySpec(start, defaultMutable = isMutable)
	} else {
		val (extension, name) = parseExtensionAndPropertyName()
		funExtension = extension
		parseSinglePropertySpec(start, isMutable, name)
	}
	val initializer = if (cursor.match(ASSIGN)) parseExpressionChain() else null
	val isLazy = LAZY isIn meta.modifiers
	val isLate = LATE isIn meta.modifiers
	when {
		cursor.match(LBRACE) -> {
			if (isLate) {
				syntaxError("计算属性不支持声明 late 修饰符", meta.modifiers.first { it.kind == LATE })
			}
			if (isLazy) {
				val node = { meta.modifiers.first { it.kind == LAZY } }
				when {
					propertySpec is DestructurePropertySpec -> syntaxError("lazy 延迟初始化属性不支持解构属性列表", node())
					isMutable -> syntaxError("lazy 延迟初始化属性必须使用 val 修饰符", node())
				}
			} else {
				when {
					propertySpec is DestructurePropertySpec -> syntaxError("计算属性不支持解构属性列表", cursor.previous)
					isMutable -> syntaxError("计算属性必须使用 val 修饰符", cursor.previous)
				}
			}
			if (initializer != null) {
				syntaxError("计算属性不支持初始化值", cursor.previous)
			}
			val getterStart = cursor.previous.location
			val body = parseStatements()
			val end = cursor.previous.location
			return PropertyDeclaration(
				propertySpec = propertySpec,
				modifiers = meta.modifiers,
				typeSpec = meta.typeSpec,
				contextSpec = meta.contextSpec,
				annotationCalls = meta.annotationCalls,
				extension = funExtension,
				location = start span end,
				getter = PropertyGetter(
					modifiers = emptyList(),
					oldValue = null,
					body = body,
					location = getterStart span end
				)
			)
		}
		
		isLazy -> syntaxError("lazy 延迟初始化属性缺少 '{'", cursor.previous.location.end)
	}
	var getter = parsePropertyGetter(isTopLevel)
	val setter = parsePropertySetter(isTopLevel)
	if (getter == null) {
		getter = parsePropertyGetter(isTopLevel)
	}
	
	if (propertySpec is DestructurePropertySpec) {
		if (isLate) {
			syntaxError("late 延迟初始化属性不支持使用解构参数列表", propertySpec)
		}
		if (initializer == null) {
			syntaxError("解构参数列表必须设置初始化值", propertySpec)
		}
		if (getter != null) {
			syntaxError("get 属性访问器不支持使用解构参数列表", propertySpec)
		}
		if (setter != null) {
			syntaxError("set 属性赋值器不支持使用解构参数列表", propertySpec)
		}
	}
	
	if (isLate) {
		if (!isMutable) {
			syntaxError("late 延迟初始化属性必须使用 var 修饰符", meta.modifiers.first { it.kind == VAL })
		}
		if (initializer != null) {
			syntaxError("late 延迟初始化属性不允许有初始化值", initializer)
		}
		if (getter != null) {
			syntaxError("late 延迟初始化属性不允许使用 get 属性访问器", getter)
		}
		if (setter != null) {
			syntaxError("late 延迟初始化属性不允许使用 set 属性赋值器", setter)
		}
	}
	
	if (setter == null && getter == null) {
		if (initializer == null && (propertySpec as SinglePropertySpec).property.type == null) {
			syntaxError("属性缺少类型", propertySpec.location.end)
		}
		if (!isLate && initializer == null) {
			syntaxError("属性缺少初始化值", propertySpec.location.end)
		}
		if (meta.contextSpec != null) {
			syntaxError("普通属性不支持 context 上下文参数", meta.contextSpec)
		}
		if (meta.typeSpec != null) {
			syntaxError("普通属性不支持定义泛型", meta.typeSpec)
		}
	}
	
	if (isMutable) {
		if (initializer == null) {
			if (funExtension != null) {
				if (getter == null) {
					syntaxError("扩展属性缺少 get 属性访问器", propertySpec)
				}
				if (setter == null) {
					syntaxError("扩展属性缺少 set 属性赋值器", propertySpec)
				}
				if (getter.oldValue != null) {
					syntaxError("扩展属性不允许在 get 属性访问器中使用 oldValue 值", getter.oldValue!!)
				}
				if (setter.oldValue != null) {
					syntaxError("扩展属性不允许在 set 属性赋值器中使用 oldValue 值", setter.oldValue!!)
				}
			}
			if (getter != null) {
				if (setter == null) {
					syntaxError("属性缺少初始化值", propertySpec)
				}
				if (getter.oldValue != null) {
					syntaxError("属性缺少初始化值, 你在 get 属性访问器中使用了 oldValue 值", propertySpec)
				}
			}
			if (setter != null) {
				if (getter == null) {
					syntaxError("属性缺少初始化值", propertySpec)
				}
				if (setter.oldValue != null) {
					syntaxError("属性缺少初始化值, 你在 set 属性赋值器中使用了 oldValue 值", propertySpec)
				}
			}
		} else {
			if (funExtension != null) {
				syntaxError("扩展属性不允许设置初始化值", initializer)
			}
			if (setter != null && getter != null && (setter.oldValue == null || getter.oldValue == null)) {
				syntaxError("在 get 属性访问器和 set 属性赋值器中未使用到 oldValue 值, 不允许有初始化值", initializer)
			}
		}
	} else {
		if (setter != null) {
			syntaxError("不可变属性不允许使用 set 属性赋值器", setter)
		}
		if (initializer != null) {
			if (funExtension != null) {
				syntaxError("扩展属性不允许设置初始化值", initializer)
			}
			if (getter != null) {
				syntaxError("不可变属性不允许同时设置初始化值和 get 属性访问器", initializer)
			}
		}
		if (getter?.oldValue != null) {
			syntaxError("不可变属性不允许在 get 属性访问器中使用 oldValue 值", getter.oldValue!!)
		}
	}
	
	val end = cursor.previous.location
	return PropertyDeclaration(
		propertySpec = propertySpec,
		modifiers = meta.modifiers,
		typeSpec = meta.typeSpec,
		contextSpec = meta.contextSpec,
		annotationCalls = meta.annotationCalls,
		extension = funExtension,
		location = start span end,
		initializer = initializer,
		getter = getter,
		setter = setter,
	)
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parsePropertyGetter(isTopLevel: Boolean): PropertyGetter? {
	val modifiers = parseModifiers()
	if (!cursor.match(GET)) {
		if (modifiers.isNotEmpty()) {
			if (VAR isIn modifiers || VAL isIn modifiers) {
				cursor.retreat(modifiers.size - 1)
			} else {
				cursor.retreat(modifiers.size)
			}
		}
		return null
	}
	if (isTopLevel) {
		modifiers.check(ModifierTarget.PROPERTY_GETTER)
	} else {
		modifiers.check(ModifierTarget.MEMBER_PROPERTY_GETTER)
	}
	val start = cursor.previous.location
	cursor.expect(LPAREN, "get 缺少 '('")
	val oldValue = if (!cursor.match(RPAREN)) {
		val name = parseIdentifier(IdentifierTarget.GETTER_PARAMETER)
		val type = if (cursor.match(COLON)) {
			parseTypeReference(allowLambda = true)
		} else null
		cursor.expect(RPAREN, "get 缺少 ')")
		ParameterReference(name, type)
	} else null
	val body = when {
		cursor.match(ASSIGN) -> listOf(parseStatement())
		cursor.match(LBRACE) -> parseStatements()
		else -> syntaxError("get 缺少函数体", cursor.previous)
	}
	val end = cursor.previous.location
	return PropertyGetter(
		modifiers = modifiers,
		oldValue = oldValue,
		body = body,
		location = start span end
	)
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parsePropertySetter(isTopLevel: Boolean): PropertySetter? {
	val modifiers = parseModifiers()
	if (!cursor.match(SET)) {
		if (modifiers.isNotEmpty()) {
			if (VAR isIn modifiers || VAL isIn modifiers) {
				cursor.retreat(modifiers.size - 1)
			} else {
				cursor.retreat(modifiers.size)
			}
		}
		return null
	}
	if (isTopLevel) {
		modifiers.check(ModifierTarget.PROPERTY_SETTER)
	} else {
		modifiers.check(ModifierTarget.MEMBER_PROPERTY_SETTER)
	}
	val start = cursor.previous.location
	cursor.expect(LPAREN, "set 缺少 '('")
	val name = parseIdentifier(IdentifierTarget.SETTER_PARAMETER)
	val type = if (cursor.match(COLON)) {
		parseTypeReference(allowLambda = true)
	} else null
	var newValue = ParameterReference(name, type)
	val oldValue = when {
		cursor.match(RPAREN) -> null
		cursor.match(COMMA) -> newValue.also {
			val name = parseIdentifier(IdentifierTarget.SETTER_PARAMETER)
			val type = parseTypeReference(allowLambda = true)
			newValue = ParameterReference(name, type)
		}
		
		else -> syntaxError("set 缺少 ')'", cursor.current)
	}
	val body = if (cursor.match(LBRACE)) {
		parseStatements()
	} else syntaxError("set 缺少函数体", cursor.previous)
	val end = cursor.previous.location
	return PropertySetter(
		modifiers = modifiers,
		oldValue = oldValue,
		newValue = newValue,
		body = body,
		location = start span end
	)
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseExtensionAndPropertyName(): Pair<TypeReference?, Identifier> {
	var name = parseIdentifier(IdentifierTarget.PROPERTY)
	if (!cursor.check { it.kind == DOT || it.kind == QUESTION_DOT || it.kind == LT }) {
		return null to name
	}
	cursor.retreat()
	var extension = parseTypeReference()
	val type = extension.type
	if (type is LambdaType || (type is NamedType && type.typeArguments.isNotEmpty())) {
		extension = when {
			cursor.match(DOT) -> extension
			cursor.match(QUESTION_DOT) -> extension.copy(
				isNullable = true,
				location = cursor.previous.location.copy(end = { it - 1 })
			)
			
			else -> syntaxError("扩展属性缺少 '.'", cursor.current)
		}
		
		name = parseIdentifier(IdentifierTarget.PROPERTY)
		return extension to name
	}
	type as NamedType
	val segments = type.segments.toMutableList()
	val segment = segments.removeLast()
	extension = extension.copy(
		type = NamedType(segments, extension.location span cursor.offset(-3).location)
	)
	name = Identifier(segment, cursor.previous.location)
	return extension to name
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseSinglePropertySpec(
	start: SourceLocation,
	isMutable: Boolean,
	name: Identifier,
): SinglePropertySpec {
	val type = if (cursor.match(COLON)) parseTypeReference(allowLambda = true) else null
	val end = cursor.previous.location
	return SinglePropertySpec(
		property = Property(
			isMutable = isMutable,
			name = name,
			type = type,
			location = start span end
		)
	)
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseDestructurePropertySpec(
	start: SourceLocation,
	defaultMutable: Boolean,
): DestructurePropertySpec {
	val properties = buildList {
		while (!cursor.match(RBRACKET)) {
			val start = cursor.current.location
			var isMutable = when {
				cursor.match(VAR) -> true
				cursor.match(VAL) -> false
				else -> null
			}
			val name = parseIdentifier(IdentifierTarget.PROPERTY_DESTRUCTURE)
			if (isMutable == null) {
				isMutable = if (name.isAnonymousBinding) false else defaultMutable
			}
			if (isMutable!! && name.isAnonymousBinding) {
				syntaxError("匿名解构属性不允许使用 var 可变修饰符", cursor.offset(-2))
			}
			val type = if (cursor.match(COLON)) {
				parseTypeReference(allowLambda = true)
			} else null
			val end = cursor.previous.location
			this += Property(
				isMutable = isMutable,
				name = name,
				type = type,
				location = start span end
			)
			if (!cursor.check(RBRACKET)) {
				cursor.expect(COMMA, "解构属性列表缺少 ','")
			}
		}
	}
	if (properties.isEmpty()) {
		syntaxError("解构属性列表缺少属性", cursor.previous)
	}
	if (properties.all { it.name.isAnonymousBinding }) {
		syntaxError("解构属性列表不允许全部使用匿名绑定", cursor.previous)
	}
	val end = cursor.previous.location
	return DestructurePropertySpec(properties, start span end)
}