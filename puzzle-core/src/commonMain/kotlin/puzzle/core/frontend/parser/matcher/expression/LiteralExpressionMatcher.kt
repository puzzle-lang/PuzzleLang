package puzzle.core.frontend.parser.matcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.parser.expression.parseLiteralExpression
import puzzle.core.frontend.parser.parser.expression.parsePostfixExpression
import puzzle.core.frontend.token.kinds.LiteralKind

object LiteralExpressionMatcher : ExpressionMatcher, NoPrefixExpressionParser<Expression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		val kind = cursor.current.kind
		return if (kind is LiteralKind) {
			cursor.advance()
			true
		} else false
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun prefixError(): Nothing {
		syntaxError("字面量前不允许有表达式", cursor.previous)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(): Expression {
		val expression = parseLiteralExpression()
		return parsePostfixExpression(expression)
	}
}