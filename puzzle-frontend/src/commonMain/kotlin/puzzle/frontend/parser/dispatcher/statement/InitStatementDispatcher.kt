package puzzle.frontend.parser.dispatcher.statement

import puzzle.ast.statement.InitStatement
import puzzle.core.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.statement.parseInitStatement

object InitStatementDispatcher : StatementDispatcher<InitStatement> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(): InitStatement {
		return parseInitStatement()
	}
}