package puzzle.core.frontend.parser.dispatcher.statement

import puzzle.core.frontend.ast.statement.ForStatement
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.statement.parseForStatement

object ForStatementDispatcher : StatementDispatcher<ForStatement> {
	
//	context(cursor: PzlTokenCursor)
//	override fun match(): Boolean {
//		return cursor.match(FOR) || return cursor.matchLabel(FOR)
//	}
//
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(): ForStatement {
		return parseForStatement()
	}
}