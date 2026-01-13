package puzzle.core.frontend.parser.dispatcher.statement

import puzzle.core.frontend.ast.statement.WhileStatement
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.statement.parseWhileStatement

object WhileStatementDispatcher : StatementDispatcher<WhileStatement> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(): WhileStatement {
		return parseWhileStatement()
	}
}