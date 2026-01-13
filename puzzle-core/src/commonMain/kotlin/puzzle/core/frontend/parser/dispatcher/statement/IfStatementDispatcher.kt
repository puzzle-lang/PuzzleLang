package puzzle.core.frontend.parser.dispatcher.statement

import puzzle.core.frontend.ast.statement.Statement
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.statement.parseIfStatement

object IfStatementDispatcher : StatementDispatcher<Statement> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(): Statement {
		return parseIfStatement()
	}
}