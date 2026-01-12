package puzzle.core.frontend.parser.dispatcher.statement

import puzzle.core.frontend.ast.statement.Statement
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.statement.parseIfStatement
import puzzle.core.frontend.token.kinds.ControlFlowKind.IF

object IfStatementMatcher : StatementMatcher<Statement> {
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(IF)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(): Statement {
		return parseIfStatement()
	}
}