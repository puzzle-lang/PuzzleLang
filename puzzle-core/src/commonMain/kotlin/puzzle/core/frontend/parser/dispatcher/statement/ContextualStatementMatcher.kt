package puzzle.core.frontend.parser.dispatcher.statement

import puzzle.core.frontend.ast.statement.ContextualStatement
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.statement.parseContextualStatement
import puzzle.core.frontend.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.frontend.token.kinds.ContextualKind.SUPER
import puzzle.core.frontend.token.kinds.ContextualKind.THIS

object ContextualStatementMatcher : StatementMatcher<ContextualStatement> {
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(THIS, LPAREN) || cursor.match(SUPER, LPAREN)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(): ContextualStatement {
		return parseContextualStatement()
	}
}