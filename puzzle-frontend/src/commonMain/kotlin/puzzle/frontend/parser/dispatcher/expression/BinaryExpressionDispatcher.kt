package puzzle.frontend.parser.dispatcher.expression

import puzzle.ast.expression.BinaryExpression
import puzzle.ast.expression.Expression
import puzzle.core.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.parseBinaryExpression
import puzzle.frontend.parser.syntaxError
import puzzle.token.kinds.OperatorKind.*

object BinaryExpressionDispatcher : ExpressionDispatcher<BinaryExpression> {
	
	val operators = setOf(
		PLUS, MINUS, STAR, SLASH, PERCENT, DOUBLE_STAR,
		RANGE_TO, RANGE_UNTIL,
		SHL, SHR, USHR,
		EQUALS, NOT_EQUALS, GT, GT_EQUALS, LT, LT_EQUALS, TRIPLE_EQUALS, TRIPLE_NOT_EQUALS,
		IN, NOT_IN,
		BIT_AND, BIT_OR, BIT_XOR,
		AND, OR
	)
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): BinaryExpression {
		if (left == null) {
			syntaxError("'${cursor.previous.value}' 前未解析到表达式", cursor.previous)
		}
		return parseBinaryExpression(left)
	}
}