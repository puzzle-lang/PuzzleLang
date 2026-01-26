package puzzle.frontend.parser.parser.expression

import puzzle.ast.expression.ElvisExpression
import puzzle.ast.expression.Expression
import puzzle.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor

context(_: FileContext, cursor: PzlTokenCursor)
fun parseElvisExpression(left: Expression): ElvisExpression {
	val right = parseExpressionChain()
	return ElvisExpression(left, right)
}