package puzzle.core.frontend.parser.parser.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.expression.MultiValueExpression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.token.kinds.BracketKind.End.RBRACKET
import puzzle.core.frontend.token.kinds.SeparatorKind

context(_: FileContext, cursor: PzlTokenCursor)
fun parseMultiValueExpression(): MultiValueExpression {
	val start = cursor.previous.location
	val expressions = buildList {
		while (!cursor.match(RBRACKET)) {
			this += parseExpressionChain()
			if (!cursor.check(RBRACKET)) {
				cursor.expect(SeparatorKind.COMMA, "多值表达式缺少 ','")
			}
		}
	}
	if (expressions.isEmpty()) {
		syntaxError("多值表达式不允许为空", cursor.previous)
	}
	if (expressions.size == 1) {
		syntaxError("多值表达式需要至少2个值", cursor.previous)
	}
	expressions.forEach {
		if (it is MultiValueExpression) {
			syntaxError("多值表达式不支持嵌套", it)
		}
	}
	val end = cursor.previous.location
	return MultiValueExpression(
		expressions = expressions,
		location = start span end
	)
}