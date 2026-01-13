package puzzle.core.frontend.parser.dispatcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.ast.expression.LambdaExpression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.parseLambdaExpression

object LambdaExpressionDispatcher : ExpressionDispatcher<LambdaExpression> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): LambdaExpression {
		if (left != null) {
			syntaxError("lambda 前不允许有表达式", cursor.previous)
		}
		return parseLambdaExpression()
	}
}