package puzzle.core.frontend.parser.parser.statement

import puzzle.core.frontend.ast.statement.BreakStatement
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.IdentifierTarget
import puzzle.core.frontend.parser.parser.expression.parseIdentifier
import puzzle.core.frontend.parser.parser.expression.tryParseExpressionChain
import puzzle.core.frontend.token.kinds.SymbolTokenKind.AT

context(_: FileContext, cursor: PzlTokenCursor)
fun parseBreakStatement(): BreakStatement {
	val start = cursor.previous.location
	val label = if (cursor.match(AT)) parseIdentifier(IdentifierTarget.LABEL) else null
	val expression = tryParseExpressionChain()
	val end = cursor.previous.location
	return BreakStatement(label, expression, start span end)
}