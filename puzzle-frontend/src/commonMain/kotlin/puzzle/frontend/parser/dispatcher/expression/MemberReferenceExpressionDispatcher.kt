package puzzle.frontend.parser.dispatcher.expression

import puzzle.ast.expression.Expression
import puzzle.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.parseMemberReferenceExpression
import puzzle.frontend.parser.parser.expression.parsePostfixExpression
import puzzle.frontend.parser.syntaxError

object MemberReferenceExpressionDispatcher : ExpressionDispatcher<Expression> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): Expression {
		if (left != null) {
			syntaxError("语法错误", cursor.previous)
		}
		val expression = parseMemberReferenceExpression(receiver = null)
		return parsePostfixExpression(expression)
	}
}