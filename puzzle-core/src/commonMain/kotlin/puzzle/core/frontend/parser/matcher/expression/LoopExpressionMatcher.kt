package puzzle.core.frontend.parser.matcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.ast.expression.LoopExpression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.matchLabel
import puzzle.core.frontend.parser.parser.expression.parseLoopExpression
import puzzle.core.frontend.token.kinds.ControlFlowKind.LOOP

object LoopExpressionMatcher : ExpressionMatcher, NoPrefixExpressionParser<LoopExpression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return cursor.match(LOOP) || cursor.matchLabel(LOOP)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun prefixError(): Nothing {
		syntaxError("loop 前不允许有表达式", cursor.previous)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(): LoopExpression {
		return parseLoopExpression()
	}
}