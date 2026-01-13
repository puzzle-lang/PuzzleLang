package puzzle.core.frontend.parser.dispatcher.statement

import puzzle.core.frontend.ast.statement.VariableDeclarationStatement
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.statement.parseVariableDeclarationStatement

object VariableDeclarationStatementDispatcher : StatementDispatcher<VariableDeclarationStatement> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(): VariableDeclarationStatement {
		return parseVariableDeclarationStatement()
	}
}