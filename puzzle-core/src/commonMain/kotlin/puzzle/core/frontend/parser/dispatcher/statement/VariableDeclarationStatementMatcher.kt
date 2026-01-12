package puzzle.core.frontend.parser.dispatcher.statement

import puzzle.core.frontend.ast.statement.VariableDeclarationStatement
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.statement.parseVariableDeclarationStatement
import puzzle.core.frontend.token.kinds.ModifierKind.VAL
import puzzle.core.frontend.token.kinds.ModifierKind.VAR

object VariableDeclarationStatementMatcher : StatementMatcher<VariableDeclarationStatement> {
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match { it.kind == VAR || it.kind == VAL }
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(): VariableDeclarationStatement {
		return parseVariableDeclarationStatement()
	}
}