package puzzle.core.frontend.parser.parser.statement

import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.ast.expression.IfExpression
import puzzle.core.frontend.ast.statement.ExpressionStatement
import puzzle.core.frontend.ast.statement.IfStatement
import puzzle.core.frontend.ast.statement.Statement
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.parser.expression.parseExpressionChain
import puzzle.core.frontend.parser.parser.expression.parsePostfixExpression
import puzzle.core.frontend.token.kinds.BracketKind.End.RPAREN
import puzzle.core.frontend.token.kinds.BracketKind.Start.LBRACE
import puzzle.core.frontend.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.frontend.token.kinds.ControlFlowKind.ELSE

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
			thenStatements = thenStatements,
			elseStatements = elseStatements,
			location = start span end
		)
		expression = parsePostfixExpression(expression)
		expression = parseExpressionChain(expression)
		ExpressionStatement(expression)
	} else {
		val end = cursor.previous.location
		IfStatement(
			condition = condition,
			thenStatements = thenStatements,
			location = start span end,
		)
	}
}