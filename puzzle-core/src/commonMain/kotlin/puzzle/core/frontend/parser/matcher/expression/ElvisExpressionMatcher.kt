package puzzle.core.frontend.parser.matcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.model.PzlContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.ast.expression.ElvisExpression
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.parser.parser.expression.parseElvisExpression
import puzzle.core.frontend.token.kinds.SymbolTokenKind.ELVIS

object ElvisExpressionMatcher : ExpressionMatcher, RequirePrefixExpressionParser<ElvisExpression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return cursor.match(ELVIS)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun prefixError(): Nothing {
		syntaxError("'?:' 前未解析到表达式", cursor.previous)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression): ElvisExpression {
		return parseElvisExpression(left)
	}
}