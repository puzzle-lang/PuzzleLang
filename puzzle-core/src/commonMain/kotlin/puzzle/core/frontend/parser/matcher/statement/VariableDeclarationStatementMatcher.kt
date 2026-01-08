package puzzle.core.frontend.parser.matcher.statement

import puzzle.core.frontend.ast.statement.VariableDeclarationStatement
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.statement.parseVariableDeclarationStatement
import puzzle.core.frontend.token.kinds.BracketKind.Start.LBRACKET
import puzzle.core.frontend.token.kinds.ModifierKind.VAL
import puzzle.core.frontend.token.kinds.ModifierKind.VAR

object VariableDeclarationStatementMatcher : StatementMatcher<VariableDeclarationStatement> {
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match { it.kind == VAR || it.kind == VAL || it.kind == LBRACKET }
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(): VariableDeclarationStatement {
		return parseVariableDeclarationStatement()
	}
}