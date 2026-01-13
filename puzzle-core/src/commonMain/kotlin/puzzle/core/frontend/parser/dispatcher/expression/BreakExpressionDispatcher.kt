package puzzle.core.frontend.parser.dispatcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.expression.BreakExpression
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.parseBreakExpression

object BreakExpressionDispatcher : ExpressionDispatcher<BreakExpression> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): BreakExpression {
		if (left != null) {
			syntaxError("break 前不允许有表达式", cursor.previous)
		}
		return parseBreakExpression()
	}
}