package puzzle.core.frontend.parser.matcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.expression.AsExpression
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.parseAsExpression
import puzzle.core.frontend.token.kinds.SymbolTokenKind.QUESTION
import puzzle.core.frontend.token.kinds.TypeOperatorKind.AS

object AsExpressionMatcher : ExpressionMatcher, RequirePrefixExpressionParser<AsExpression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return cursor.match(AS)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun prefixError(): Nothing {
		val isSafe = cursor.check(QUESTION)
		syntaxError(
			"${if (isSafe) "as?" else "as"} 前未解析到表达式",
			cursor.offset(if (isSafe) -2 else -1)
		)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression): AsExpression {
		return parseAsExpression(left)
	}
}