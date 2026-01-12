package puzzle.core.frontend.parser.parser.expression

import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.ast.expression.OracleExpression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor

context(_: FileContext, cursor: PzlTokenCursor)
fun parseOracleExpression(left: Expression): OracleExpression {
	val right = parseExpressionChain()
	return OracleExpression(left, right)
}