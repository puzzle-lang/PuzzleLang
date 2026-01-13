package puzzle.core.frontend.parser.dispatcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.ast.expression.MatchExpression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.parseMatchExpression

object MatchExpressionDispatcher : ExpressionDispatcher<MatchExpression> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): MatchExpression {
		if (left != null) {
			syntaxError("match 前不允许有表达式", cursor.previous)
		}
		return parseMatchExpression()
	}
}