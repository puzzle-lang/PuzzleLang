package puzzle.frontend.parser.parser.declaration

import puzzle.ast.declaration.*
import puzzle.ast.expression.Identifier
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
import puzzle.frontend.parser.parser.expression.IdentifierTarget
import puzzle.frontend.parser.parser.expression.tryParseIdentifier
import puzzle.frontend.parser.parser.parameter.parameter.ParameterTarget
import puzzle.frontend.parser.parser.parameter.parameter.parseParameters
import puzzle.frontend.parser.parser.statement.parseStatements
import puzzle.frontend.parser.parser.type.parseTypeReference
import puzzle.frontend.parser.syntaxError
import puzzle.token.kinds.AccessKind.DOT
import puzzle.token.kinds.AccessKind.QUESTION_DOT
import puzzle.token.kinds.AssignmentKind.*
import puzzle.token.kinds.BracketKind
import puzzle.token.kinds.BracketKind.End.RBRACKET
import puzzle.token.kinds.BracketKind.Start.LBRACKET
import puzzle.token.kinds.OperatorKind.*
import puzzle.token.kinds.SeparatorKind.COMMA
import puzzle.token.kinds.SymbolKind
import puzzle.token.kinds.SymbolKind.COLON
import puzzle.token.kinds.SymbolKind.QUESTION

context(_: FileContext, cursor: PzlTokenCursor)
fun parseFunDeclaration(meta: DeclarationMeta, start: SourceLocation): FunDeclaration {
	val (extension, funName) = parseExtensionAndFunName()
	val parameters = parseParameters(ParameterTarget.FUN)
	val containsErrorsSpec = meta.errorsSpec != null
	val returnSpec = when {
		!cursor.match(COLON) -> null
		cursor.match(LBRACKET) -> {
			val start = cursor.previous.location
			val types = buildList {
				while (!cursor.match(RBRACKET)) {
					val type = parseTypeReference(allowLambda = true)
					if (containsErrorsSpec && type.isNullable) {
						syntaxError("使用 errors 后不能返回可空类型", type.location.end)
					}
					this += type
					if (!cursor.check(RBRACKET)) {
						cursor.expect(COMMA, "多返回值类型列表缺少 ','")
					}
				}
			}
			if (types.isEmpty()) {
				syntaxError("多返回值缺少类型", cursor.previous)
			}
			if (types.size == 1) {
				syntaxError("多返回值至少需要2个类型", cursor.previous)
			}
			if (cursor.match(QUESTION)) {
				syntaxError("语法错误", cursor.previous)
			}
			val end = cursor.previous.location
			MultiReturnSpec(types, start span end)
		}
		
		else -> {
			val type = parseTypeReference(allowLambda = true)
			if (containsErrorsSpec && type.isNullable) {
				syntaxError("使用 errors 后不能返回可空类型", type.location.end)
			}
			SingleReturnSpec(type)
		}
	}
	val expressions = if (cursor.match(BracketKind.Start.LBRACE)) parseStatements() else null
	val end = cursor.previous.location
	return FunDeclaration(
		name = funName,
		docComment = meta.docComment,
		parameters = parameters,
		modifiers = meta.modifiers,
		returnSpec = returnSpec,
		extension = extension,
		typeSpec = meta.typeSpec,
		contextSpec = meta.contextSpec,
		errorsSpec = meta.errorsSpec,
		annotationCalls = meta.annotationCalls,
		body = expressions,
		location = start span end
	)
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseExtensionAndFunName(): Pair<TypeReference?, FunName> {
	val name = tryParseIdentifier(IdentifierTarget.FUN) ?: run {
		val funName = tryParseOperatorFunName()
			?: syntaxError("函数缺少名称", cursor.current)
		return null to funName
	}
	if (!cursor.check { it.kind == DOT || it.kind == QUESTION_DOT || it.kind == LT }) {
		return null to IdentifierFunName(name)
	}
	cursor.retreat()
	var extension = parseTypeReference(allowLambda = true)
	val type = extension.type
	if (type is LambdaType || (type is NamedType && type.typeArguments.isNotEmpty())) {
		extension = when {
			cursor.match(DOT) -> extension
			cursor.match(QUESTION_DOT) -> extension.copy(
				isNullable = true,
				location = cursor.previous.location.copy(end = { it - 1 })
			)
			
			else -> syntaxError("扩展函数缺少 '.'", cursor.current)
		}
		val funName = tryParseIdentifier(IdentifierTarget.FUN)?.let { IdentifierFunName(it) }
			?: tryParseOperatorFunName()
			?: syntaxError("函数缺少名称", cursor.current)
		return extension to funName
	}
	type as NamedType
	if (cursor.match { it.kind == DOT || it.kind == QUESTION_DOT }) {
		extension = extension.copy(
			isNullable = true,
			location = cursor.previous.location.copy(end = { it - 1 })
		)
		val funName = tryParseIdentifier(IdentifierTarget.FUN)?.let { IdentifierFunName(it) }
			?: tryParseOperatorFunName()
			?: syntaxError("函数缺少名称", cursor.current)
		return extension to funName
	}
	val segments = type.segments.toMutableList()
	val segment = segments.removeLast()
	extension = extension.copy(
		type = NamedType(segments, extension.location span cursor.offset(-3).location)
	)
	val funName = IdentifierFunName(Identifier(segment, cursor.previous.location))
	return extension to funName
}

private val overloadableSymbols = setOf(
	PLUS, MINUS, NOT, BIT_NOT, DOUBLE_PLUS, DOUBLE_MINUS,
	STAR, SLASH, PERCENT, DOUBLE_STAR,
	EQUALS,
	IN,
	BIT_AND, BIT_OR, BIT_XOR, SHL, SHR, USHR,
	PLUS_ASSIGN, MINUS_ASSIGN, STAR_ASSIGN, SLASH_ASSIGN, PERCENT_ASSIGN,
	RANGE_TO, RANGE_UNTIL,
)

private val notOverloadableSymbols = setOf(
	TRIPLE_EQUALS, TRIPLE_NOT_EQUALS,
	AND, OR,
	NOT_EQUALS, GT, GT_EQUALS, LT, LT_EQUALS,
	NOT_EQUALS
)

context(_: FileContext, cursor: PzlTokenCursor)
private fun tryParseOperatorFunName(): FunName? {
	if (cursor.match { it.kind in overloadableSymbols }) {
		val token = cursor.previous
		val kind = token.kind as SymbolKind
		val name = Identifier(kind.value, token.location)
		return SymbolFunName(name, kind)
	}
	
	val start = cursor.current.location
	val kind = when {
		cursor.match(LBRACKET, RBRACKET) -> {
			if (cursor.match(ASSIGN)) {
				MagicKind.SETTER
			} else {
				MagicKind.GETTER
			}
		}
		
		cursor.match(LT_EQUALS, GT) -> MagicKind.COMPARE
		
		cursor.match { it.kind in notOverloadableSymbols } -> {
			val token = cursor.previous
			syntaxError("'${token.value}' 运算符不支持被重载", token)
		}
		
		else -> return null
	}
	val name = Identifier(kind.value, start span cursor.previous.location)
	return MagicFunName(name, kind)
}