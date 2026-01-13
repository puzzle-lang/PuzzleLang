package puzzle.core.frontend.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.SymbolToken
import puzzle.core.frontend.ast.declaration.*
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.ast.type.LambdaType
import puzzle.core.frontend.ast.type.NamedType
import puzzle.core.frontend.ast.type.TypeReference
import puzzle.core.frontend.ast.type.copy
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.model.copy
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.parser.expression.IdentifierTarget
import puzzle.core.frontend.parser.parser.expression.tryParseIdentifier
import puzzle.core.frontend.parser.parser.parameter.parameter.ParameterTarget
import puzzle.core.frontend.parser.parser.parameter.parameter.parseParameters
import puzzle.core.frontend.parser.parser.statement.parseStatements
import puzzle.core.frontend.parser.parser.type.parseTypeReference
import puzzle.core.frontend.token.kinds.AccessKind.DOT
import puzzle.core.frontend.token.kinds.AccessKind.QUESTION_DOT
import puzzle.core.frontend.token.kinds.AssignmentKind.*
import puzzle.core.frontend.token.kinds.BracketKind.End.RBRACKET
import puzzle.core.frontend.token.kinds.BracketKind.Start.LBRACE
import puzzle.core.frontend.token.kinds.BracketKind.Start.LBRACKET
import puzzle.core.frontend.token.kinds.OperatorKind.*
import puzzle.core.frontend.token.kinds.SeparatorKind.COMMA
import puzzle.core.frontend.token.kinds.SymbolKind
import puzzle.core.frontend.token.kinds.SymbolKind.COLON
import puzzle.core.frontend.token.kinds.SymbolKind.QUESTION

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
	val expressions = if (cursor.match(LBRACE)) parseStatements() else null
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
		return SymbolFunName(SymbolToken(token.kind as SymbolKind, token.location))
	}
	when {
		cursor.match(LBRACKET, RBRACKET, ASSIGN) -> {
			val location = cursor.offset(-3).location span cursor.previous.location
			return MagicFunName(MagicKind.SETTER, location)
		}
		
		cursor.match(LBRACKET, RBRACKET) -> {
			val location = cursor.offset(-2).location span cursor.previous.location
			return MagicFunName(MagicKind.GETTER, location)
		}
		
		cursor.match(LT_EQUALS, GT) -> {
			val location = cursor.offset(-2).location span cursor.previous.location
			return MagicFunName(MagicKind.COMPARE, location)
		}
	}
	if (cursor.match { it.kind in notOverloadableSymbols }) {
		val token = cursor.previous
		syntaxError("'${token.value}' 运算符不支持被重载", cursor.previous)
	}
	return null
}