package puzzle.core.frontend.parser.dispatcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.ast.expression.ReturnExpression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.parseReturnExpression
import puzzle.core.frontend.token.kinds.JumpKind.RETURN

object ReturnExpressionMatcher : ExpressionMatcher, NoPrefixExpressionParser<ReturnExpression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return cursor.match(RETURN)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun prefixError(): Nothing {
		syntaxError("return 前不允许有表达式", cursor.previous)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(): ReturnExpression {
		return parseReturnExpression()
	}
}