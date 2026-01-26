package puzzle.frontend.parser.dispatcher.statement

import puzzle.ast.statement.ContextualStatement
import puzzle.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.statement.parseContextualStatement

object ContextualStatementDispatcher : StatementDispatcher<ContextualStatement> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(): ContextualStatement {
		return parseContextualStatement()
	}
}