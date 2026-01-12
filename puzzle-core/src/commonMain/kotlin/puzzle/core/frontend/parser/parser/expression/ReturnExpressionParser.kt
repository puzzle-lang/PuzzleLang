package puzzle.core.frontend.parser.parser.expression

import puzzle.core.frontend.ast.expression.ReturnExpression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.token.kinds.SymbolTokenKind.AT

context(_: FileContext, cursor: PzlTokenCursor)
fun parseReturnExpression(): ReturnExpression {
	val start = cursor.previous.location
	val label = if (cursor.match(AT)) parseIdentifier(IdentifierTarget.LABEL) else null
	val expression = tryParseExpressionChain()
	val end = cursor.previous.location
	return ReturnExpression(label, expression, start span end)
}