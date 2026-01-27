package puzzle.frontend.parser.parser.statement

import puzzle.ast.statement.Statement
import puzzle.core.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.statement.*
import puzzle.frontend.parser.parser.expression.matchIdentifier
import puzzle.token.kinds.BracketKind.End.RBRACE
import puzzle.token.kinds.BracketKind.Start.LPAREN
import puzzle.token.kinds.ContextualKind.*
import puzzle.token.kinds.ControlFlowKind.*
import puzzle.token.kinds.ModifierKind.VAL
import puzzle.token.kinds.ModifierKind.VAR
import puzzle.token.kinds.SymbolKind.AT

context(_: FileContext, cursor: PzlTokenCursor)
fun parseStatement(): Statement {
	val dispatcher = parseStatementDispatcher()
	return dispatcher.parse()
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseStatementDispatcher(): StatementDispatcher<*> {
	var dispatcher = when (cursor.current.kind) {
		VAR, VAL -> VariableDeclarationStatementDispatcher
		THIS if cursor.nextOrNull?.kind == LPAREN -> {
			cursor.advance()
			ContextualStatementDispatcher
		}
		
		SUPER if cursor.nextOrNull?.kind == LPAREN -> {
			cursor.advance()
			ContextualStatementDispatcher
		}
		
		INIT -> InitStatementDispatcher
		IF -> IfStatementDispatcher
		FOR -> ForStatementDispatcher
		DO, WHILE -> WhileStatementDispatcher
		else -> null
	}
	if (dispatcher != null) {
		cursor.advance()
		return dispatcher
	}
	if (!cursor.matchIdentifier()) {
		return OtherStatementDispatcher
	}
	if (!cursor.match(AT)) {
		cursor.retreat()
		return OtherStatementDispatcher
	}
	dispatcher = when (cursor.current.kind) {
		DO, WHILE -> WhileStatementDispatcher
		FOR -> ForStatementDispatcher
		else -> {
			cursor.retreat(2)
			return OtherStatementDispatcher
		}
	}
	cursor.advance()
	return dispatcher
}

context(_: FileContext, cursor: PzlTokenCursor)
fun parseStatements(): List<Statement> {
	if (cursor.match(RBRACE)) return emptyList()
	return buildList {
		do {
			this += parseStatement()
		} while (!cursor.match(RBRACE))
	}
}