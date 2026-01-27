package puzzle.frontend.parser.dispatcher.expression

import puzzle.ast.expression.Expression
import puzzle.ast.expression.TernaryExpression
import puzzle.core.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.parseTernaryExpression
import puzzle.frontend.parser.syntaxError

object TernaryExpressionDispatcher : ExpressionDispatcher<TernaryExpression> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): TernaryExpression {
		if (left == null) {
			syntaxError("'?' 前未解析到表达式", cursor.previous)
		}
		return parseTernaryExpression(left)
	}
}