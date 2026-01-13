package puzzle.core.frontend.parser.dispatcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.ast.expression.MultiValueExpression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.parseMultiValueExpression

object MultiValueExpressionDispatcher : ExpressionDispatcher<MultiValueExpression> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): MultiValueExpression {
		if (left != null) {
			syntaxError("'[' 前不允许有表达式", cursor.previous)
		}
		return parseMultiValueExpression()
	}
}