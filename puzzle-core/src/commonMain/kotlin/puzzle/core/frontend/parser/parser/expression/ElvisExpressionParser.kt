package puzzle.core.frontend.parser.parser.expression

import puzzle.core.frontend.ast.expression.ElvisExpression
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor

context(_: FileContext, cursor: PzlTokenCursor)
fun parseElvisExpression(left: Expression): ElvisExpression {
	val right = parseExpressionChain()
	return ElvisExpression(left, right)
}