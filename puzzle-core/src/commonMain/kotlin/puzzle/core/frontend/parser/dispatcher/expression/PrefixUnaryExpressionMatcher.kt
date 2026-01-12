package puzzle.core.frontend.parser.dispatcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.ast.expression.PrefixUnaryExpression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.parsePrefixUnaryExpression
import puzzle.core.frontend.token.kinds.OperatorKind.*

object PrefixUnaryExpressionMatcher : ExpressionMatcher, NoPrefixExpressionParser<PrefixUnaryExpression> {
	
	private val kinds = setOf(
		PLUS,
		MINUS,
		NOT,
		BIT_NOT,
		DOUBLE_PLUS,
		DOUBLE_MINUS
	)
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return cursor.match {
			it.kind in kinds && (left == null || (it.kind != PLUS && it.kind != MINUS))
		}
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun prefixError(): Nothing {
		syntaxError("'${cursor.previous.value}' 前不允许有表达式", cursor.previous)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(): PrefixUnaryExpression {
		return parsePrefixUnaryExpression()
	}
}