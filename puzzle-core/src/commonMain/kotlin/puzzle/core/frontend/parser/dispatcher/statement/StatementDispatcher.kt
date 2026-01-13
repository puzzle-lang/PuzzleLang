package puzzle.core.frontend.parser.dispatcher.statement

import puzzle.core.frontend.ast.statement.Statement
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor

sealed interface StatementDispatcher<S : Statement> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	fun parse(): S
}