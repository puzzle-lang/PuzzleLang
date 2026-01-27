package puzzle.frontend.parser.parser.expression

import puzzle.ast.expression.Expression
import puzzle.ast.expression.OracleExpression
import puzzle.core.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor

context(_: FileContext, cursor: PzlTokenCursor)
fun parseOracleExpression(left: Expression): OracleExpression {
	val right = parseExpressionChain()
	return OracleExpression(left, right)
}