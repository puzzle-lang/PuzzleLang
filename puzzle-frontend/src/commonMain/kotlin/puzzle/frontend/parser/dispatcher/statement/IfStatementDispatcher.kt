package puzzle.frontend.parser.dispatcher.statement

import puzzle.ast.statement.Statement
import puzzle.core.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.statement.parseIfStatement

object IfStatementDispatcher : StatementDispatcher<Statement> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(): Statement {
		return parseIfStatement()
	}
}