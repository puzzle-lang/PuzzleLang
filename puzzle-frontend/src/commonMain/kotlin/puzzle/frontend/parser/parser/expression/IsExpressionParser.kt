package puzzle.frontend.parser.parser.expression

import puzzle.ast.expression.Expression
import puzzle.ast.expression.IsExpression
import puzzle.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.type.parseTypeReference
import puzzle.token.kinds.OperatorKind.NOT

context(_: FileContext, cursor: PzlTokenCursor)
fun parseIsExpression(left: Expression): IsExpression {
	val negated = cursor.offset(-2).kind == NOT
	val type = parseTypeReference()
	return IsExpression(left, type, negated)
}