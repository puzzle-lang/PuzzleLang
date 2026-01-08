package puzzle.core.frontend.parser.matcher.statement

import puzzle.core.frontend.ast.statement.InitStatement
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.statement.parseInitStatement
import puzzle.core.frontend.token.kinds.ContextualKind.INIT

object InitStatementMatcher : StatementMatcher<InitStatement> {
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(INIT)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(): InitStatement {
		return parseInitStatement()
	}
}