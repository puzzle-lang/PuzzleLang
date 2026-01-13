package puzzle.core.frontend.parser.parser.statement

import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.ast.statement.InitStatement
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.parser.expression.ArgumentTarget
import puzzle.core.frontend.parser.parser.expression.parseArguments
import puzzle.core.frontend.parser.parser.type.parseNamedType
import puzzle.core.frontend.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.frontend.token.kinds.SymbolKind.QUESTION

context(_: FileContext, cursor: PzlTokenCursor)
fun parseInitStatement(): InitStatement {
	val start = cursor.previous.location
	val isSafe = cursor.match(QUESTION)
	val type = parseNamedType()
	cursor.expect(LPAREN, "${if (isSafe) "init?" else "init"} 语句缺少 '('")
	val arguments = parseArguments(ArgumentTarget.CALL)
	val end = cursor.previous.location
	return InitStatement(type, arguments, isSafe, start span end)
}