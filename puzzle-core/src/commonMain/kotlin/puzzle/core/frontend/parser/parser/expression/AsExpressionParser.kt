package puzzle.core.frontend.parser.parser.expression

import puzzle.core.frontend.ast.expression.AsExpression
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.type.parseTypeReference
import puzzle.core.frontend.token.kinds.SymbolKind.QUESTION

context(_: FileContext, cursor: PzlTokenCursor)
fun parseAsExpression(left: Expression): AsExpression {
	val isSafe = cursor.match(QUESTION)
	val type = parseTypeReference()
	return AsExpression(left, type, isSafe)
}