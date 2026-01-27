package puzzle.frontend.parser.parser.statement

import puzzle.ast.statement.InitStatement
import puzzle.core.context.FileContext
import puzzle.core.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.ArgumentTarget
import puzzle.frontend.parser.parser.expression.parseArguments
import puzzle.frontend.parser.parser.type.parseNamedType
import puzzle.token.kinds.BracketKind.Start.LPAREN
import puzzle.token.kinds.SymbolKind.QUESTION

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