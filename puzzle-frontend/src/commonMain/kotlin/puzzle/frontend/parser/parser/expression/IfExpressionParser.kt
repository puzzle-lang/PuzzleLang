package puzzle.frontend.parser.parser.expression

import puzzle.ast.expression.IfExpression
import puzzle.context.FileContext
import puzzle.base.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.statement.parseStatement
import puzzle.frontend.parser.parser.statement.parseStatements
import puzzle.token.kinds.BracketKind.End.RPAREN
import puzzle.token.kinds.BracketKind.Start.LBRACE
import puzzle.token.kinds.BracketKind.Start.LPAREN
import puzzle.token.kinds.ControlFlowKind.ELSE

context(_: FileContext, cursor: PzlTokenCursor)
fun parseIfExpression(): IfExpression {
	val start = cursor.previous.location
	cursor.expect(LPAREN, "if 表达式缺少 '('")
	val condition = parseExpressionChain()
	cursor.expect(RPAREN, "if 表达式缺少 ')'")
	val thenStatements = if (cursor.match(LBRACE)) {
		parseStatements()
	} else {
		listOf(parseStatement())
	}
	cursor.expect(ELSE, "if 表达式缺少 else")
	val elseStatements = if (cursor.match(LBRACE)) {
		parseStatements()
	} else {
		listOf(parseStatement())
	}
	val end = cursor.previous.location
	return IfExpression(
		condition = condition,
		thenBody = thenStatements,
		elseBody = elseStatements,
		location = start span end
	)
}