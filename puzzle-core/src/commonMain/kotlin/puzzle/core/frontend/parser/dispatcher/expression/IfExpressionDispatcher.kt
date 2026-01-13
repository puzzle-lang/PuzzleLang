package puzzle.core.frontend.parser.dispatcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.ast.expression.IfExpression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.parseIfExpression

object IfExpressionDispatcher : ExpressionDispatcher<IfExpression> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): IfExpression {
		if (left != null) {
			syntaxError("if 前不允许有表达式", cursor.previous)
		}
		return parseIfExpression()
	}
}