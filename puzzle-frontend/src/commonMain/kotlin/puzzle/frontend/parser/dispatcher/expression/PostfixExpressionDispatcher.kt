package puzzle.frontend.parser.dispatcher.expression

import puzzle.ast.expression.Expression
import puzzle.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.parsePostfixExpression
import puzzle.frontend.parser.syntaxError

object PostfixExpressionDispatcher : ExpressionDispatcher<Expression> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): Expression {
		if (left != null) {
			syntaxError("标识符前不允许是标识符", cursor.previous)
		}
		return parsePostfixExpression()
	}
}