package puzzle.core.frontend.parser.dispatcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.parseGroupingExpression
import puzzle.core.frontend.parser.parser.expression.parsePostfixExpression

object GroupingExpressionDispatcher : ExpressionDispatcher<Expression> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): Expression {
		if (left != null) {
			syntaxError("'(' 前不允许有表达式", cursor.previous)
		}
		val expression = parseGroupingExpression()
		return parsePostfixExpression(expression)
	}
}