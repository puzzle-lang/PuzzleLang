package puzzle.frontend.parser.dispatcher.expression

import puzzle.ast.expression.Expression
import puzzle.ast.expression.ReturnExpression
import puzzle.core.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.parseReturnExpression
import puzzle.frontend.parser.syntaxError

object ReturnExpressionDispatcher : ExpressionDispatcher<ReturnExpression> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): ReturnExpression {
		if (left != null) {
			syntaxError("return 前不允许有表达式", cursor.previous)
		}
		return parseReturnExpression()
	}
}