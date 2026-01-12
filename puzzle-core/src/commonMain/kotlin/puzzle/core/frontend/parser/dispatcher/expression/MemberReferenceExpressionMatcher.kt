package puzzle.core.frontend.parser.dispatcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.parseMemberReferenceExpression
import puzzle.core.frontend.parser.parser.expression.parsePostfixExpression
import puzzle.core.frontend.token.kinds.AccessKind.DOUBLE_COLON

object MemberReferenceExpressionMatcher : ExpressionMatcher, NoPrefixExpressionParser<Expression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return cursor.match(DOUBLE_COLON)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun prefixError(): Nothing {
		syntaxError("语法错误", cursor.previous)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(): Expression {
		val expression = parseMemberReferenceExpression(receiver = null)
		return parsePostfixExpression(expression)
	}
}