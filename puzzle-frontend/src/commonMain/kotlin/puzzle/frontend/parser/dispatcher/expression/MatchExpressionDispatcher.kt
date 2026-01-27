package puzzle.frontend.parser.dispatcher.expression

import puzzle.ast.expression.Expression
import puzzle.ast.expression.MatchExpression
import puzzle.core.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.parseMatchExpression
import puzzle.frontend.parser.syntaxError

object MatchExpressionDispatcher : ExpressionDispatcher<MatchExpression> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): MatchExpression {
		if (left != null) {
			syntaxError("match 前不允许有表达式", cursor.previous)
		}
		return parseMatchExpression()
	}
}