package puzzle.core.frontend.parser.parser.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.model.PzlContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.ast.expression.TernaryExpression
import puzzle.core.frontend.token.kinds.SymbolTokenKind.COLON

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseTernaryExpression(condition: Expression): TernaryExpression {
	val thenExpression = parseExpressionChain()
	if (!cursor.match(COLON)) {
		syntaxError("三元运算符缺少 ':'", cursor.current)
	}
	val elseExpression = parseExpressionChain()
	return TernaryExpression(condition, thenExpression, elseExpression)
}