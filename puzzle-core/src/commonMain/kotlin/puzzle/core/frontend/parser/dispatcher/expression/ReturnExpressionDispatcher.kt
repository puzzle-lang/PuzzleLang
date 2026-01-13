package puzzle.core.frontend.parser.dispatcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.ast.expression.ReturnExpression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.parseReturnExpression

object ReturnExpressionDispatcher : ExpressionDispatcher<ReturnExpression> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): ReturnExpression {
		if (left != null) {
			syntaxError("return 前不允许有表达式", cursor.previous)
		}
		return parseReturnExpression()
	}
}