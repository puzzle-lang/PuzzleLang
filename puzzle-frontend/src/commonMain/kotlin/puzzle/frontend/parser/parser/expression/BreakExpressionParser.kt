package puzzle.frontend.parser.parser.expression

import puzzle.ast.expression.BreakExpression
import puzzle.core.context.FileContext
import puzzle.core.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.token.kinds.SymbolKind.AT

context(_: FileContext, cursor: PzlTokenCursor)
fun parseBreakExpression(): BreakExpression {
	val start = cursor.previous.location
	val label = if (cursor.match(AT)) parseIdentifier(IdentifierTarget.LABEL) else null
	val expression = tryParseExpressionChain()
	val end = cursor.previous.location
	return BreakExpression(label, expression, start span end)
}