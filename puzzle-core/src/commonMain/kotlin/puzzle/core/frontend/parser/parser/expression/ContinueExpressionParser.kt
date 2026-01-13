package puzzle.core.frontend.parser.parser.expression

import puzzle.core.frontend.ast.expression.ContinueExpression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.token.kinds.SymbolKind.AT

context(_: FileContext, cursor: PzlTokenCursor)
fun parseContinueExpression(): ContinueExpression {
	val start = cursor.previous.location
	val label = if (cursor.match(AT)) parseIdentifier(IdentifierTarget.LABEL) else null
	val end = cursor.previous.location
	return ContinueExpression(label, start span end)
}