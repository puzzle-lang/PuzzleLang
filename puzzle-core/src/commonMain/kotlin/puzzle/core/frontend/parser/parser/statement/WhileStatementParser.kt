package puzzle.core.frontend.parser.parser.statement

import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.ast.statement.WhileKind
import puzzle.core.frontend.ast.statement.WhileStatement
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.parseExpressionChain
import puzzle.core.frontend.parser.parser.expression.toIdentifier
import puzzle.core.frontend.token.kinds.BracketKind.End.RPAREN
import puzzle.core.frontend.token.kinds.BracketKind.Start.LBRACE
import puzzle.core.frontend.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.frontend.token.kinds.ControlFlowKind.DO
import puzzle.core.frontend.token.kinds.ControlFlowKind.WHILE
import puzzle.core.frontend.token.kinds.SymbolKind.AT

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