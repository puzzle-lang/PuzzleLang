package puzzle.core.frontend.parser.dispatcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.parser.expression.parseGroupingExpression
import puzzle.core.frontend.parser.parser.expression.parsePostfixExpression
import puzzle.core.frontend.token.kinds.BracketKind.Start.LPAREN

object GroupingExpressionMatcher : ExpressionMatcher, NoPrefixExpressionParser<Expression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return cursor.match(LPAREN)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun prefixError(): Nothing {
		syntaxError("'(' 前不允许有表达式", cursor.previous)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(): Expression {
		val expression = parseGroupingExpression()
		return parsePostfixExpression(expression)
	}
}