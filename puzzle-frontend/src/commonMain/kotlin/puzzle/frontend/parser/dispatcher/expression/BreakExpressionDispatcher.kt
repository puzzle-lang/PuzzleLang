package puzzle.frontend.parser.dispatcher.expression

import puzzle.ast.expression.BreakExpression
import puzzle.ast.expression.Expression
import puzzle.core.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.parseBreakExpression
import puzzle.frontend.parser.syntaxError

object BreakExpressionDispatcher : ExpressionDispatcher<BreakExpression> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): BreakExpression {
		if (left != null) {
			syntaxError("break 前不允许有表达式", cursor.previous)
		}
		return parseBreakExpression()
	}
}