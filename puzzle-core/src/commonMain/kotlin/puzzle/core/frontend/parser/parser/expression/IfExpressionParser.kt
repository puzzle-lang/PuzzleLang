package puzzle.core.frontend.parser.parser.expression

import puzzle.core.frontend.ast.expression.IfExpression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.statement.parseStatement
import puzzle.core.frontend.parser.parser.statement.parseStatements
import puzzle.core.frontend.token.kinds.BracketKind.End.RPAREN
import puzzle.core.frontend.token.kinds.BracketKind.Start.LBRACE
import puzzle.core.frontend.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.frontend.token.kinds.ControlFlowKind.ELSE

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
		thenStatements = thenStatements,
		elseStatements = elseStatements,
		location = start span end
	)
}