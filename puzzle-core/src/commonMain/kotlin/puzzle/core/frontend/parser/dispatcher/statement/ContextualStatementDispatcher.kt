package puzzle.core.frontend.parser.dispatcher.statement

import puzzle.core.frontend.ast.statement.ContextualStatement
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.statement.parseContextualStatement

object ContextualStatementDispatcher : StatementDispatcher<ContextualStatement> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(): ContextualStatement {
		return parseContextualStatement()
	}
}