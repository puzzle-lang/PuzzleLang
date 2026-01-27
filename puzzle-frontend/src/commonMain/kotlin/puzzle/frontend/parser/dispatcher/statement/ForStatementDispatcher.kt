package puzzle.frontend.parser.dispatcher.statement

import puzzle.ast.statement.ForStatement
import puzzle.core.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.statement.parseForStatement

object ForStatementDispatcher : StatementDispatcher<ForStatement> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(): ForStatement {
		return parseForStatement()
	}
}