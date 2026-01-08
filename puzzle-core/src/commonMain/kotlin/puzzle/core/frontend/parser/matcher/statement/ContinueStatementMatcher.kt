package puzzle.core.frontend.parser.matcher.statement

import puzzle.core.frontend.ast.statement.ContinueStatement
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.statement.parseContinueStatement
import puzzle.core.frontend.token.kinds.JumpKind.CONTINUE

object ContinueStatementMatcher : StatementMatcher<ContinueStatement> {
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(CONTINUE)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(): ContinueStatement {
		return parseContinueStatement()
	}
}