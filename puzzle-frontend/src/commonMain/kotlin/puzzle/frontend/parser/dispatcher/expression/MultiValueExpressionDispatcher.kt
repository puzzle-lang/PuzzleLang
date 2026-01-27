package puzzle.frontend.parser.dispatcher.expression

import puzzle.ast.expression.Expression
import puzzle.ast.expression.MultiValueExpression
import puzzle.core.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.parseMultiValueExpression
import puzzle.frontend.parser.syntaxError

object MultiValueExpressionDispatcher : ExpressionDispatcher<MultiValueExpression> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): MultiValueExpression {
		if (left != null) {
			syntaxError("'[' 前不允许有表达式", cursor.previous)
		}
		return parseMultiValueExpression()
	}
}