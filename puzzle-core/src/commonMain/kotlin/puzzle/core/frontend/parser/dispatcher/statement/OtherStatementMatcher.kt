package puzzle.core.frontend.parser.dispatcher.statement

import puzzle.core.frontend.ast.statement.Statement
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.statement.parseOtherStatement

object OtherStatementMatcher : StatementMatcher<Statement> {
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean = true
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(): Statement {
		return parseOtherStatement()
	}
}