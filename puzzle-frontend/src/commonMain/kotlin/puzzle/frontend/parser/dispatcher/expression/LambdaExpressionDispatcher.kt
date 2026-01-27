package puzzle.frontend.parser.dispatcher.expression

import puzzle.ast.expression.Expression
import puzzle.ast.expression.LambdaExpression
import puzzle.core.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.parseLambdaExpression
import puzzle.frontend.parser.syntaxError

object LambdaExpressionDispatcher : ExpressionDispatcher<LambdaExpression> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): LambdaExpression {
		if (left != null) {
			syntaxError("lambda 前不允许有表达式", cursor.previous)
		}
		return parseLambdaExpression()
	}
}