package puzzle.core.frontend.parser.dispatcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.ast.expression.LoopExpression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.parseLoopExpression

object LoopExpressionDispatcher : ExpressionDispatcher<LoopExpression> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): LoopExpression {
		if (left != null) {
			syntaxError("loop 前不允许有表达式", cursor.previous)
		}
		return parseLoopExpression()
	}
}