package puzzle.frontend.parser.parser.expression

import puzzle.ast.expression.AsExpression
import puzzle.ast.expression.Expression
import puzzle.core.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.type.parseTypeReference
import puzzle.token.kinds.SymbolKind.QUESTION

context(_: FileContext, cursor: PzlTokenCursor)
fun parseAsExpression(left: Expression): AsExpression {
	val isSafe = cursor.match(QUESTION)
	val type = parseTypeReference()
	return AsExpression(left, type, isSafe)
}