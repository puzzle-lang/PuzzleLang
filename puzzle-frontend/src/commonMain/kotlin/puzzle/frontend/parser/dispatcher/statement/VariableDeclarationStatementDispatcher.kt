package puzzle.frontend.parser.dispatcher.statement

import puzzle.ast.statement.VariableDeclarationStatement
import puzzle.core.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.statement.parseVariableDeclarationStatement

object VariableDeclarationStatementDispatcher : StatementDispatcher<VariableDeclarationStatement> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(): VariableDeclarationStatement {
		return parseVariableDeclarationStatement()
	}
}