package puzzle.frontend.parser.dispatcher.expression

import puzzle.ast.expression.Expression
import puzzle.core.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.parseLiteralExpression
import puzzle.frontend.parser.parser.expression.parsePostfixExpression
import puzzle.frontend.parser.syntaxError

object LiteralExpressionDispatcher : ExpressionDispatcher<Expression> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): Expression {
		if (left != null) {
			syntaxError("字面量前不允许有表达式", cursor.previous)
		}
		val expression = parseLiteralExpression()
		return parsePostfixExpression(expression)
	}
}