package puzzle.frontend.parser.parser.statement

import puzzle.ast.expression.Expression
import puzzle.ast.statement.WhileKind
import puzzle.ast.statement.WhileStatement
import puzzle.core.context.FileContext
import puzzle.core.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.parseExpressionChain
import puzzle.frontend.parser.parser.expression.toIdentifier
import puzzle.token.kinds.BracketKind.End.RPAREN
import puzzle.token.kinds.BracketKind.Start.LBRACE
import puzzle.token.kinds.BracketKind.Start.LPAREN
import puzzle.token.kinds.ControlFlowKind.DO
import puzzle.token.kinds.ControlFlowKind.WHILE
import puzzle.token.kinds.SymbolKind.AT

context(_: FileContext, cursor: PzlTokenCursor)
fun parseWhileStatement(): WhileStatement {
	val containsLabel = cursor.offset(-2).kind == AT
	val start = if (containsLabel) cursor.offset(-3).location else cursor.previous.location
	val label = if (containsLabel) cursor.offset(-3).toIdentifier() else null
	val kind = if (cursor.previous.kind == DO) WhileKind.DO_WHILE else WhileKind.WHILE
	var condition: Expression? = null
	if (kind == WhileKind.WHILE) {
		cursor.expect(LPAREN, "while 语句缺少 '('")
		condition = parseExpressionChain()
		cursor.expect(RPAREN, "while 语句缺少 ')'")
	}
	val body = if (cursor.match(LBRACE)) {
		parseStatements()
	} else {
		listOf(parseStatement())
	}
	if (kind == WhileKind.DO_WHILE) {
		cursor.expect(WHILE, "do 语句缺少 while")
		cursor.expect(LPAREN, "while 语句缺少 '('")
		condition = parseExpressionChain()
		cursor.expect(RPAREN, "while 语句缺少 ')'")
	}
	val end = cursor.previous.location
	return WhileStatement(label, condition!!, kind, body, start span end)
}