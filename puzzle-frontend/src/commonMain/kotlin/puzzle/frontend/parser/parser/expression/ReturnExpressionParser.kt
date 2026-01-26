package puzzle.frontend.parser.parser.expression

import puzzle.ast.expression.ReturnExpression
import puzzle.context.FileContext
import puzzle.base.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.token.kinds.SymbolKind.AT

context(_: FileContext, cursor: PzlTokenCursor)
fun parseReturnExpression(): ReturnExpression {
	val start = cursor.previous.location
	val label = if (cursor.match(AT)) parseIdentifier(IdentifierTarget.LABEL) else null
	val expression = tryParseExpressionChain()
	val end = cursor.previous.location
	return ReturnExpression(label, expression, start span end)
}