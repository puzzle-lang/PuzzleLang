package puzzle.frontend.parser.parser.statement

import puzzle.ast.expression.Expression
import puzzle.ast.expression.IfExpression
import puzzle.ast.statement.ExpressionStatement
import puzzle.ast.statement.IfStatement
import puzzle.ast.statement.Statement
import puzzle.context.FileContext
import puzzle.base.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.parseExpressionChain
import puzzle.frontend.parser.parser.expression.parsePostfixExpression
import puzzle.token.kinds.BracketKind.End.RPAREN
import puzzle.token.kinds.BracketKind.Start.LBRACE
import puzzle.token.kinds.BracketKind.Start.LPAREN
import puzzle.token.kinds.ControlFlowKind.ELSE

context(_: FileContext, cursor: PzlTokenCursor)
fun parseIfStatement(): Statement {
	val start = cursor.previous.location
	cursor.expect(LPAREN, "if 语句缺少 '('")
	val condition = parseExpressionChain()
	cursor.expect(RPAREN, "if 语句缺少 ')'")
	val thenStatements = if (cursor.match(LBRACE)) {
		parseStatements()
	} else {
		listOf(parseStatement())
	}
	return if (cursor.match(ELSE)) {
		val elseStatements = if (cursor.match(LBRACE)) {
			parseStatements()
		} else {
			listOf(parseStatement())
		}
		val end = cursor.previous.location
		var expression: Expression = IfExpression(
			condition = condition,
			thenBody = thenStatements,
			elseBody = elseStatements,
			location = start span end
		)
		expression = parsePostfixExpression(expression)
		expression = parseExpressionChain(expression)
		ExpressionStatement(expression)
	} else {
		val end = cursor.previous.location
		IfStatement(
			condition = condition,
			thenBody = thenStatements,
			location = start span end,
		)
	}
}