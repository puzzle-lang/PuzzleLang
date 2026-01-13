package puzzle.core.frontend.parser.dispatcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.parseMemberReferenceExpression
import puzzle.core.frontend.parser.parser.expression.parsePostfixExpression

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