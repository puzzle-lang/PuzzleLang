package puzzle.frontend.parser.dispatcher.expression

import puzzle.ast.expression.ContinueExpression
import puzzle.ast.expression.Expression
import puzzle.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.parseContinueExpression
import puzzle.frontend.parser.syntaxError

object ContinueExpressionDispatcher : ExpressionDispatcher<ContinueExpression> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): ContinueExpression {
		if (left != null) {
			syntaxError("continue 前不允许有表达式", cursor.previous)
		}
		return parseContinueExpression()
	}
}