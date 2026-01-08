package puzzle.core.frontend.parser.parser.expression

import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.ast.expression.IsExpression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.type.parseTypeReference
import puzzle.core.frontend.token.kinds.OperatorKind.NOT

context(_: FileContext, cursor: PzlTokenCursor)
fun parseIsExpression(left: Expression): IsExpression {
	val negated = cursor.offset(-2).kind == NOT
	val type = parseTypeReference()
	return IsExpression(left, type, negated)
}