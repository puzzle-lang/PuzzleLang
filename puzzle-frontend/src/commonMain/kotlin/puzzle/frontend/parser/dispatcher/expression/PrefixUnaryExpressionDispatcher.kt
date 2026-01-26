package puzzle.frontend.parser.dispatcher.expression

import puzzle.ast.expression.Expression
import puzzle.ast.expression.PrefixUnaryExpression
import puzzle.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.parsePrefixUnaryExpression
import puzzle.frontend.parser.syntaxError
import puzzle.token.kinds.OperatorKind.*

object PrefixUnaryExpressionDispatcher : ExpressionDispatcher<PrefixUnaryExpression> {
	
	val kinds = setOf(
		PLUS,
		MINUS,
		NOT,
		BIT_NOT,
		DOUBLE_PLUS,
		DOUBLE_MINUS
	)
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): PrefixUnaryExpression {
		if (left != null) {
			syntaxError("'${cursor.previous.value}' 前不允许有表达式", cursor.previous)
		}
		return parsePrefixUnaryExpression()
	}
}