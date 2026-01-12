package puzzle.core.frontend.parser.dispatcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.expression.ContinueExpression
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.parseContinueExpression
import puzzle.core.frontend.token.kinds.JumpKind.CONTINUE

object ContinueExpressionMatcher : ExpressionMatcher, NoPrefixExpressionParser<ContinueExpression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return cursor.match(CONTINUE)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun prefixError(): Nothing {
		syntaxError("continue 前不允许有表达式", cursor.previous)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(): ContinueExpression {
		return parseContinueExpression()
	}
}