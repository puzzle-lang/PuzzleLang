package puzzle.core.frontend.parser.parser.statement

import puzzle.core.frontend.ast.statement.BreakStatement
import puzzle.core.frontend.model.PzlContext
import puzzle.core.frontend.model.equalsLine
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.IdentifierTarget
import puzzle.core.frontend.parser.parser.expression.parseExpressionSpec
import puzzle.core.frontend.parser.parser.expression.parseIdentifier
import puzzle.core.frontend.token.kinds.SymbolKind.AT

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseBreakStatement(): BreakStatement {
	val start = cursor.previous.location
	val label = if (cursor.match(AT)) parseIdentifier(IdentifierTarget.LABEL) else null
	val expressionSpec = if (cursor.previous.equalsLine(cursor.current)) {
		parseExpressionSpec()
	} else null
	val end = cursor.previous.location
	return BreakStatement(label, expressionSpec, start span end)
}