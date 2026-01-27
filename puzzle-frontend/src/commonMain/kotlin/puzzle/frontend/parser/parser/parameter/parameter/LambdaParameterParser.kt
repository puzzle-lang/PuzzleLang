package puzzle.frontend.parser.parser.parameter.parameter

import puzzle.ast.parameter.LambdaParameter
import puzzle.core.context.FileContext
import puzzle.core.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.IdentifierTarget
import puzzle.frontend.parser.parser.expression.parseIdentifier
import puzzle.frontend.parser.parser.parseAnnotationCalls
import puzzle.frontend.parser.parser.parseModifiers
import puzzle.frontend.parser.parser.type.parseTypeReference
import puzzle.frontend.parser.syntaxError
import puzzle.token.kinds.AssignmentKind.ASSIGN
import puzzle.token.kinds.BracketKind.End.RPAREN
import puzzle.token.kinds.BracketKind.Start.LPAREN
import puzzle.token.kinds.SeparatorKind.COMMA
import puzzle.token.kinds.SymbolKind.COLON

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