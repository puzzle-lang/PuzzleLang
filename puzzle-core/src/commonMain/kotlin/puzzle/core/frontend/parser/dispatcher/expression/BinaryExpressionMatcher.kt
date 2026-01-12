package puzzle.core.frontend.parser.dispatcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.expression.BinaryExpression
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.parseBinaryExpression
import puzzle.core.frontend.token.kinds.OperatorKind.*

object BinaryExpressionMatcher : ExpressionMatcher, RequirePrefixExpressionParser<BinaryExpression> {
	
	private val operators = setOf(
		PLUS, MINUS, STAR, SLASH, PERCENT, DOUBLE_STAR,
		RANGE_TO, RANGE_UNTIL,
		SHL, SHR, USHR,
		EQUALS, NOT_EQUALS, GT, GT_EQUALS, LT, LT_EQUALS, TRIPLE_EQUALS, TRIPLE_NOT_EQUALS,
		IN, NOT_IN,
		BIT_AND, BIT_OR, BIT_XOR,
		AND, OR
	)
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun prefixError(): Nothing {
		syntaxError("'${cursor.previous.value}' 前未解析到表达式", cursor.previous)
	}
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return cursor.match { it.kind in operators }
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression): BinaryExpression {
		return parseBinaryExpression(left)
	}
}