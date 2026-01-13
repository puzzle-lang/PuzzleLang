package puzzle.core.frontend.parser.dispatcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.expression.ContinueExpression
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.parseContinueExpression

object ContinueExpressionDispatcher : ExpressionDispatcher<ContinueExpression> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): ContinueExpression {
		if (left != null) {
			syntaxError("continue 前不允许有表达式", cursor.previous)
		}
		return parseContinueExpression()
	}
}