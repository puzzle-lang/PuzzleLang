package puzzle.core.frontend.parser.matcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.ast.expression.IsExpression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.parseIsExpression
import puzzle.core.frontend.token.kinds.OperatorKind.NOT
import puzzle.core.frontend.token.kinds.TypeOperatorKind.IS

object IsExpressionMatcher : ExpressionMatcher, RequirePrefixExpressionParser<IsExpression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return cursor.match(NOT, IS) || cursor.match(IS)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun prefixError(): Nothing {
		val negated = cursor.offset(-2).kind == NOT
		syntaxError(
			"${if (negated) "!is" else "is"} 前未解析到表达式",
			cursor.offset(if (negated) -2 else -1)
		)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression): IsExpression {
		return parseIsExpression(left)
	}
}