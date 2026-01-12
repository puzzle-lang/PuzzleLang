package puzzle.core.frontend.parser.matcher.statement

import puzzle.core.frontend.ast.statement.Statement
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor

sealed interface StatementMatcher<S : Statement> {
	
	companion object {
		
		val matchers = arrayOf(
			VariableDeclarationStatementMatcher,
			ContextualStatementMatcher,
			InitStatementMatcher,
			IfStatementMatcher,
			ForStatementMatcher,
			WhileStatementMatcher,
			OtherStatementMatcher
		)
	}
	
	context(cursor: PzlTokenCursor)
	fun match(): Boolean
	
	context(_: FileContext, cursor: PzlTokenCursor)
	fun parse(): S
}