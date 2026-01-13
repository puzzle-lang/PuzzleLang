package puzzle.core.frontend.parser.parser.parameter.parameter

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.parameter.LambdaParameter
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.IdentifierTarget
import puzzle.core.frontend.parser.parser.expression.parseIdentifier
import puzzle.core.frontend.parser.parser.parseAnnotationCalls
import puzzle.core.frontend.parser.parser.parseModifiers
import puzzle.core.frontend.parser.parser.type.parseTypeReference
import puzzle.core.frontend.token.kinds.AssignmentKind.ASSIGN
import puzzle.core.frontend.token.kinds.BracketKind.End.RPAREN
import puzzle.core.frontend.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.frontend.token.kinds.SeparatorKind.COMMA
import puzzle.core.frontend.token.kinds.SymbolKind.COLON

context(_: FileContext, cursor: PzlTokenCursor)
fun parseLambdaParameters(): List<LambdaParameter> {
	if (!cursor.match(LPAREN)) {
		syntaxError("lambda 缺少 '('", cursor.current)
	}
	if (cursor.match(RPAREN)) return emptyList()
	return buildList {
		do {
			this += parseLambdaParameter()
			if (!cursor.check(RPAREN)) {
				cursor.expect(COMMA, "型参列表缺少 ','")
			}
		} while (!cursor.match(RPAREN))
	}
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseLambdaParameter(): LambdaParameter {
	val start = cursor.previous.location
	val annotationCalls = parseAnnotationCalls()
	if (annotationCalls.isNotEmpty()) {
		syntaxError("lambda 型参不支持注解", annotationCalls.first())
	}
	val modifiers = parseModifiers()
	if (modifiers.isNotEmpty()) {
		syntaxError("lambda 型参不支持修饰符", modifiers.first())
	}
	val name = if (cursor.offsetOrNull(1)?.kind == COLON) {
		parseIdentifier(IdentifierTarget.LAMBDA_PARAMETER).also {
			cursor.advance()
		}
	} else null
	val type = parseTypeReference(allowLambda = true)
	if (cursor.match(ASSIGN)) {
		syntaxError("lambda 型参不支持默认值", cursor.previous)
	}
	val end = cursor.previous.location
	return LambdaParameter(
		name = name,
		type = type,
		location = start span end
	)
}