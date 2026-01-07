package puzzle.core.frontend.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.model.PzlContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.model.copy
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.ast.declaration.PropertyDeclaration
import puzzle.core.frontend.ast.declaration.PropertyGetter
import puzzle.core.frontend.ast.declaration.PropertySetter
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.ast.type.LambdaType
import puzzle.core.frontend.ast.type.NamedType
import puzzle.core.frontend.ast.type.TypeReference
import puzzle.core.frontend.ast.type.copy
import puzzle.core.frontend.parser.matcher.declaration.DeclarationHeader
import puzzle.core.frontend.parser.parser.expression.IdentifierTarget
import puzzle.core.frontend.parser.parser.expression.parseExpressionChain
import puzzle.core.frontend.parser.parser.expression.parseIdentifier
import puzzle.core.frontend.parser.parser.statement.parseStatement
import puzzle.core.frontend.parser.parser.statement.parseStatements
import puzzle.core.frontend.parser.parser.type.parseTypeReference
import puzzle.core.frontend.token.kinds.AccessKind.DOT
import puzzle.core.frontend.token.kinds.AccessKind.QUESTION_DOT
import puzzle.core.frontend.token.kinds.AccessorKind.GET
import puzzle.core.frontend.token.kinds.AccessorKind.SET
import puzzle.core.frontend.token.kinds.AssignmentKind.ASSIGN
import puzzle.core.frontend.token.kinds.BracketKind.End.RPAREN
import puzzle.core.frontend.token.kinds.BracketKind.Start.LBRACE
import puzzle.core.frontend.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.frontend.token.kinds.ModifierKind.*
import puzzle.core.frontend.token.kinds.OperatorKind.LT
import puzzle.core.frontend.token.kinds.SeparatorKind.COMMA
import puzzle.core.frontend.token.kinds.SymbolTokenKind.COLON
import puzzle.core.frontend.token.kinds.isIn

context(_: PzlContext, cursor: PzlTokenCursor)
fun parsePropertyDeclaration(header: DeclarationHeader, start: SourceLocation): PropertyDeclaration {
	val (extension, name) = parseExtensionAndPropertyName()
	val type = if (cursor.match(COLON)) parseTypeReference(allowLambda = true) else null
	val initializer = if (cursor.match(ASSIGN)) parseExpressionChain() else null
	val isVal = VAL isIn header.modifiers
	val isLazy = LAZY isIn header.modifiers
	when {
		cursor.match(LBRACE) -> {
			if (!isVal) {
				if (isLazy) {
					syntaxError("lazy 延迟初始化必须使用 val 修饰符", header.modifiers.first { it.kind == LAZY })
				} else {
					syntaxError("计算属性必须使用 val 修饰符", cursor.previous)
				}
			}
			if (initializer != null) {
				syntaxError("计算属性不支持初始化值", cursor.previous)
			}
			val getterStart = cursor.previous.location
			val body = parseStatements()
			val end = cursor.previous.location
			return PropertyDeclaration(
				name = name,
				type = type,
				modifiers = header.modifiers,
				typeSpec = header.typeSpec,
				contextSpec = header.contextSpec,
				annotationCalls = header.annotationCalls,
				extension = extension,
				location = start span end,
				getter = PropertyGetter(
					oldValue = null,
					body = body,
					location = getterStart span end
				)
			)
		}
		
		isLazy -> syntaxError("lazy 延迟初始化属性缺少 '{'", cursor.previous.location.end)
	}
	
	var getter = parsePropertyGetter()
	val setter = parsePropertySetter()
	if (getter == null) {
		getter = parsePropertyGetter()
	}
	
	val isLate = LATE isIn header.modifiers
	if (isLate) {
		if (isVal) {
			syntaxError("late 延迟初始化属性必须使用 var 修饰符", header.modifiers.first { it.kind == VAL })
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
		if (initializer == null && type == null) {
			syntaxError("属性缺少类型", name.location.end)
		}
		if (!isLate && initializer == null) {
			syntaxError("属性缺少初始化值", type!!.location.end)
		}
		if (header.contextSpec != null) {
			syntaxError("普通属性不支持 context 上下文参数", header.contextSpec)
		}
		if (header.typeSpec != null) {
			syntaxError("普通属性不支持定义泛型", header.typeSpec)
		}
	}
	
	if (isVal) {
		if (setter != null) {
			syntaxError("不可变属性不允许使用 set 属性赋值器", setter)
		}
		if (initializer != null) {
			if (extension != null) {
				syntaxError("扩展属性不允许设置初始化值", initializer)
			}
			if (getter != null) {
				syntaxError("不可变属性不允许同时设置初始化值和 get 属性访问器", initializer)
			}
		}
		if (getter?.oldValue != null) {
			syntaxError("不可变属性不允许在 get 属性访问器中使用 oldValue 值", getter.oldValue)
		}
	} else {
		if (initializer == null) {
			if (extension != null) {
				if (getter == null) {
					syntaxError("扩展属性缺少 get 属性访问器", type ?: name)
				}
				if (setter == null) {
					syntaxError("扩展属性缺少 set 属性赋值器", type ?: name)
				}
				if (getter.oldValue != null) {
					syntaxError("扩展属性不允许在 get 属性访问器中使用 oldValue 值", getter.oldValue)
				}
				if (setter.oldValue != null) {
					syntaxError("扩展属性不允许在 set 属性赋值器中使用 oldValue 值", setter.oldValue)
				}
			}
			if (getter != null) {
				if (setter == null) {
					syntaxError("属性缺少初始化值", type ?: name)
				}
				if (getter.oldValue != null) {
					syntaxError("属性缺少初始化值, 你在 get 属性访问器中使用了 oldValue 值", type ?: name)
				}
			}
			if (setter != null) {
				if (getter == null) {
					syntaxError("属性缺少初始化值", type ?: name)
				}
				if (setter.oldValue != null) {
					syntaxError("属性缺少初始化值, 你在 set 属性赋值器中使用了 oldValue 值", type ?: name)
				}
			}
		} else {
			if (extension != null) {
				syntaxError("扩展属性不允许设置初始化值", initializer)
			}
			if (setter != null && getter != null && (setter.oldValue == null || getter.oldValue == null)) {
				syntaxError("在 get 属性访问器和 set 属性赋值器中未使用到 oldValue 值, 不允许有初始化值", initializer)
			}
		}
	}
	val end = cursor.previous.location
	
	return PropertyDeclaration(
		name = name,
		type = type,
		modifiers = header.modifiers,
		typeSpec = header.typeSpec,
		contextSpec = header.contextSpec,
		annotationCalls = header.annotationCalls,
		extension = extension,
		location = start span end,
		initializer = initializer,
		getter = getter,
		setter = setter,
	)
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parsePropertyGetter(): PropertyGetter? {
	if (!cursor.match(GET)) return null
	val start = cursor.previous.location
	cursor.expect(LPAREN, "get 缺少 '('")
	val oldValue = if (!cursor.match(RPAREN)) {
		parseIdentifier(IdentifierTarget.GETTER_PARAMETER).also {
			cursor.expect(RPAREN, "get 缺少 ')")
		}
	} else null
	val body = when {
		cursor.match(ASSIGN) -> listOf(parseStatement())
		cursor.match(LBRACE) -> parseStatements()
		else -> syntaxError("get 缺少函数体", cursor.previous)
	}
	val end = cursor.previous.location
	return PropertyGetter(
		oldValue = oldValue,
		body = body,
		location = start span end
	)
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parsePropertySetter(): PropertySetter? {
	if (!cursor.match(SET)) return null
	val start = cursor.previous.location
	cursor.expect(LPAREN, "set 缺少 '('")
	var newValue = parseIdentifier(IdentifierTarget.SETTER_PARAMETER)
	val oldValue = when {
		cursor.match(RPAREN) -> null
		cursor.match(COMMA) -> newValue.also {
			newValue = parseIdentifier(IdentifierTarget.SETTER_PARAMETER).also {
				cursor.expect(RPAREN, "set 缺少 ')'")
			}
		}
		
		else -> syntaxError("set 缺少 ')'", cursor.current)
	}
	val body = if (cursor.match(LBRACE)) {
		parseStatements()
	} else syntaxError("set 缺少函数体", cursor.previous)
	val end = cursor.previous.location
	return PropertySetter(
		oldValue = oldValue,
		newValue = newValue,
		body = body,
		location = start span end
	)
}

context(_: PzlContext, cursor: PzlTokenCursor)
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