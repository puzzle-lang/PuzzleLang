package puzzle.frontend.parser.parser.expression

import puzzle.ast.expression.Expression
import puzzle.ast.expression.TernaryExpression
import puzzle.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.syntaxError
import puzzle.token.kinds.SymbolKind.COLON

context(_: FileContext, cursor: PzlTokenCursor)
fun parseTernaryExpression(condition: Expression): TernaryExpression {
	val thenExpression = parseExpressionChain()
	if (!cursor.match(COLON)) {
		syntaxError("三元运算符缺少 ':'", cursor.current)
	}
	val elseExpression = parseExpressionChain()
	return TernaryExpression(condition, thenExpression, elseExpression)
}