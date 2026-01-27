package puzzle.frontend.parser.dispatcher.statement

import puzzle.ast.statement.WhileStatement
import puzzle.core.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.statement.parseWhileStatement

object WhileStatementDispatcher : StatementDispatcher<WhileStatement> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(): WhileStatement {
		return parseWhileStatement()
	}
}