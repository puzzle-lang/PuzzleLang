package puzzle.frontend.parser.dispatcher.expression

import puzzle.ast.expression.Expression
import puzzle.ast.expression.LoopExpression
import puzzle.core.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.parseLoopExpression
import puzzle.frontend.parser.syntaxError

object LoopExpressionDispatcher : ExpressionDispatcher<LoopExpression> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): LoopExpression {
		if (left != null) {
			syntaxError("loop 前不允许有表达式", cursor.previous)
		}
		return parseLoopExpression()
	}
}