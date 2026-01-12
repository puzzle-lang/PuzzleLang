package puzzle.core.frontend.parser.dispatcher.statement

import puzzle.core.frontend.ast.statement.WhileStatement
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.matchLabel
import puzzle.core.frontend.parser.parser.statement.parseWhileStatement
import puzzle.core.frontend.token.kinds.ControlFlowKind.DO
import puzzle.core.frontend.token.kinds.ControlFlowKind.WHILE

object WhileStatementMatcher : StatementMatcher<WhileStatement> {
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match { it.kind == DO || it.kind == WHILE }
				|| cursor.matchLabel(DO)
				|| cursor.matchLabel(WHILE)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(): WhileStatement {
		return parseWhileStatement()
	}
}