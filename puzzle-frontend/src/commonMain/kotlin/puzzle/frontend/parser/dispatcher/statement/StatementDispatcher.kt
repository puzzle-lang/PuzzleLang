package puzzle.frontend.parser.dispatcher.statement

import puzzle.ast.statement.Statement
import puzzle.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor

sealed interface StatementDispatcher<S : Statement> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	fun parse(): S
}