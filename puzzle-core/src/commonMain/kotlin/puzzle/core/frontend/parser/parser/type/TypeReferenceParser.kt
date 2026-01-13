package puzzle.core.frontend.parser.parser.type

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.ast.declaration.MultiReturnSpec
import puzzle.core.frontend.ast.declaration.SingleReturnSpec
import puzzle.core.frontend.ast.parameter.LambdaContextSpec
import puzzle.core.frontend.ast.parameter.LambdaParameter
import puzzle.core.frontend.ast.type.LambdaType
import puzzle.core.frontend.ast.type.NamedType
import puzzle.core.frontend.ast.type.Type
import puzzle.core.frontend.ast.type.TypeReference
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.parser.expression.checkIdentifier
import puzzle.core.frontend.parser.parser.parameter.context.parseLambdaContextSpec
import puzzle.core.frontend.parser.parser.parameter.parameter.parseLambdaParameters
import puzzle.core.frontend.token.kinds.AccessKind.DOT
import puzzle.core.frontend.token.kinds.AccessKind.QUESTION_DOT
import puzzle.core.frontend.token.kinds.BracketKind.End.RBRACKET
import puzzle.core.frontend.token.kinds.BracketKind.Start.LBRACKET
import puzzle.core.frontend.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.frontend.token.kinds.SeparatorKind
import puzzle.core.frontend.token.kinds.SymbolKind.ARROW
import puzzle.core.frontend.token.kinds.SymbolKind.QUESTION

context(_: FileContext, cursor: PzlTokenCursor)
fun parseTypeReference(
	allowLambda: Boolean = false,
	allowNullable: Boolean = true,
): TypeReference {
	val start = cursor.current.location
	val contextSpec = parseLambdaContextSpec()
	var extension = when {
		cursor.checkIdentifier() && cursor.offsetOrNull(1)?.kind != LPAREN -> {
			parseNamedType()
		}
		
		cursor.check(LPAREN) -> {
			val parameters = parseLambdaParameters()
			if (cursor.match(ARROW)) {
				if (!allowLambda) {
					syntaxError("不支持 lambda 表达式", cursor.previous)
				}
				parseLambdaType(start, contextSpec, null, parameters)
			} else {
				if (parameters.size != 1) {
					syntaxError("lambda 表达式缺少 '->'", cursor.current)
				}
				val parameter = parameters.single()
				val type = parameter.type.type
				if (!cursor.match { it.kind == DOT || it.kind == QUESTION_DOT }) {
					val isNullable = parseNullable(allowNullable) || parameter.type.isNullable
					val end = cursor.previous.location
					return TypeReference(type, isNullable, start span end)
				} else {
					val isNullable = cursor.previous.kind == QUESTION_DOT || parameter.type.isNullable
					val end = cursor.previous.location
					TypeReference(type, isNullable, start span end)
				}
			}
		}
		
		else -> syntaxError("语法错误", cursor.current)
	}
	
	extension = when (extension) {
		is TypeReference -> extension
		is Type -> {
			if (!cursor.check { it.kind == DOT || it.kind == QUESTION_DOT } || cursor.nextOrNull?.kind != LPAREN) {
				if (extension is NamedType && contextSpec != null) {
					syntaxError("不能为类型指定 context 上下文参数", extension)
				}
				val isNullable = parseNullable(allowNullable)
				val end = cursor.previous.location
				return TypeReference(extension, isNullable, start span end)
			}
			cursor.advance()
			val isNullable = cursor.previous.kind == QUESTION_DOT
			val end = cursor.previous.location
			TypeReference(extension, isNullable, start span end)
		}
		
		else -> syntaxError("未知的类型", extension)
	}
	val parameters = parseLambdaParameters()
	cursor.expect(ARROW, "lambda 表达式缺少 '->'")
	val type = parseLambdaType(start, contextSpec, extension, parameters)
	val isNullable = parseNullable(allowNullable)
	val end = cursor.previous.location
	return TypeReference(type, isNullable, start span end)
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseLambdaType(
	start: SourceLocation,
	contextSpec: LambdaContextSpec?,
	extension: TypeReference?,
	parameters: List<LambdaParameter>,
): LambdaType {
	val returnSpec = if (cursor.match(LBRACKET)) {
		val start = cursor.previous.location
		val types = buildList {
			while (!cursor.match(RBRACKET)) {
				this += parseTypeReference(true)
				if (!cursor.check(RBRACKET)) {
					cursor.expect(SeparatorKind.COMMA, "lambda 表达式多返回值列表缺少 ','")
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
			syntaxError("请为 lambda 加上 '(' 和 ')'", cursor.previous)
		}
		val end = cursor.previous.location
		MultiReturnSpec(types, start span end)
	} else {
		SingleReturnSpec(parseTypeReference(allowLambda = true))
	}
	val end = cursor.previous.location
	return LambdaType(extension, contextSpec, parameters, returnSpec, start span end)
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseNullable(allowNullable: Boolean): Boolean {
	var isNullable = false
	while (cursor.match(QUESTION)) {
		isNullable = true
		if (!allowNullable) {
			syntaxError("不支持可空类型", cursor.previous)
		}
	}
	return isNullable
}