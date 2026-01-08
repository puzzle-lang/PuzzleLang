package puzzle.core.frontend.parser.matcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.ast.expression.LambdaExpression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.matchLabel
import puzzle.core.frontend.parser.parser.expression.parseLambdaExpression
import puzzle.core.frontend.token.kinds.BracketKind.Start.LBRACE

object LambdaExpressionMatcher : ExpressionMatcher, NoPrefixExpressionParser<LambdaExpression> {
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return cursor.match(LBRACE) || cursor.matchLabel(LBRACE)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun prefixError(): Nothing {
		syntaxError("lambda 前不允许有表达式", cursor.previous)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(): LambdaExpression {
		return parseLambdaExpression()
	}
}