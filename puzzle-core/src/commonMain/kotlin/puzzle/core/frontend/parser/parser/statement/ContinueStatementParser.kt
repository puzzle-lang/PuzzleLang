package puzzle.core.frontend.parser.parser.statement

import puzzle.core.frontend.ast.statement.ContinueStatement
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.IdentifierTarget
import puzzle.core.frontend.parser.parser.expression.parseIdentifier
import puzzle.core.frontend.token.kinds.SymbolTokenKind.AT

context(_: FileContext, cursor: PzlTokenCursor)
fun parseContinueStatement(): ContinueStatement {
	val start = cursor.previous.location
	val label = if (cursor.match(AT)) parseIdentifier(IdentifierTarget.LABEL) else null
	val end = cursor.previous.location
	return ContinueStatement(label, start span end)
}