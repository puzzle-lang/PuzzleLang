package puzzle.core.frontend.parser.dispatcher.statement

import puzzle.core.frontend.ast.statement.InitStatement
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.statement.parseInitStatement

object InitStatementDispatcher : StatementDispatcher<InitStatement> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(): InitStatement {
		return parseInitStatement()
	}
}