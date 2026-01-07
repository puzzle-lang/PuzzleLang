package puzzle.core.frontend.parser.matcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.model.PzlContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.ast.expression.TernaryExpression
import puzzle.core.frontend.parser.parser.expression.parseTernaryExpression
import puzzle.core.frontend.token.kinds.SymbolTokenKind.QUESTION

object TernaryExpressionMatcher : ExpressionMatcher, RequirePrefixExpressionParser<TernaryExpression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return cursor.match(QUESTION)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun prefixError(): Nothing {
		syntaxError("'?' 前未解析到表达式", cursor.previous)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression): TernaryExpression {
		return parseTernaryExpression(left)
	}
}