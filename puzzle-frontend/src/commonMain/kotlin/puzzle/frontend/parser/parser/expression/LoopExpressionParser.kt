package puzzle.frontend.parser.parser.expression

import puzzle.ast.expression.LoopExpression
import puzzle.context.FileContext
import puzzle.base.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.statement.parseStatement
import puzzle.frontend.parser.parser.statement.parseStatements
import puzzle.token.kinds.BracketKind.Start.LBRACE
import puzzle.token.kinds.SymbolKind.AT

context(_: FileContext, cursor: PzlTokenCursor)
fun parseLoopExpression(): LoopExpression {
	val containsLabel = cursor.offset(-2).kind == AT
	val start = if (containsLabel) cursor.offset(-3).location else cursor.previous.location
	val label = if (containsLabel) cursor.offset(-3).toIdentifier() else null
	val body = if (cursor.match(LBRACE)) {
		parseStatements()
	} else {
		listOf(parseStatement())
	}
	val end = cursor.previous.location
	return LoopExpression(label, body, start span end)
}