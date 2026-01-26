package puzzle.frontend.parser.dispatcher.expression

import puzzle.ast.expression.Expression
import puzzle.ast.expression.IfExpression
import puzzle.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.parseIfExpression
import puzzle.frontend.parser.syntaxError

object IfExpressionDispatcher : ExpressionDispatcher<IfExpression> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): IfExpression {
		if (left != null) {
			syntaxError("if 前不允许有表达式", cursor.previous)
		}
		return parseIfExpression()
	}
}