package puzzle.frontend.parser.parser.expression

import puzzle.ast.expression.ContinueExpression
import puzzle.core.context.FileContext
import puzzle.core.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.token.kinds.SymbolKind.AT

context(_: FileContext, cursor: PzlTokenCursor)
fun parseContinueExpression(): ContinueExpression {
	val start = cursor.previous.location
	val label = if (cursor.match(AT)) parseIdentifier(IdentifierTarget.LABEL) else null
	val end = cursor.previous.location
	return ContinueExpression(label, start span end)
}