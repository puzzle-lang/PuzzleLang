package puzzle.core.frontend.parser.dispatcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.ast.expression.TernaryExpression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.parseTernaryExpression

object TernaryExpressionDispatcher : ExpressionDispatcher<TernaryExpression> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): TernaryExpression {
		if (left == null) {
			syntaxError("'?' 前未解析到表达式", cursor.previous)
		}
		return parseTernaryExpression(left)
	}
}