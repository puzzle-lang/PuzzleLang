package puzzle.frontend.parser.parser.expression

import puzzle.ast.expression.*
import puzzle.ast.statement.Statement
import puzzle.context.FileContext
import puzzle.base.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.statement.parseStatement
import puzzle.frontend.parser.parser.statement.parseStatements
import puzzle.frontend.parser.parser.type.parseTypeReference
import puzzle.frontend.parser.syntaxError
import puzzle.token.kinds.BracketKind.End.RBRACE
import puzzle.token.kinds.BracketKind.End.RPAREN
import puzzle.token.kinds.BracketKind.Start.LBRACE
import puzzle.token.kinds.BracketKind.Start.LPAREN
import puzzle.token.kinds.ControlFlowKind.ELSE
import puzzle.token.kinds.ControlFlowKind.IF
import puzzle.token.kinds.OperatorKind.IN
import puzzle.token.kinds.OperatorKind.NOT
import puzzle.token.kinds.SeparatorKind.COMMA
import puzzle.token.kinds.SymbolKind.ARROW
import puzzle.token.kinds.TypeOperatorKind.AS
import puzzle.token.kinds.TypeOperatorKind.IS

context(_: FileContext, cursor: PzlTokenCursor)
fun parseMatchExpression(): MatchExpression {
	return when {
		cursor.match(LPAREN) -> parseMatchPatternExpression()
		cursor.match(LBRACE) -> parseMatchConditionExpression()
		else -> syntaxError("match 语法错误", cursor.current)
	}
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseMatchPatternExpression(): MatchPatternExpression {
	val start = cursor.offset(-2).location
	val subject = parseExpressionChain()
	cursor.expect(RPAREN, "match 的匹配表达式结尾必须使用 ')'")
	cursor.expect(LBRACE, "match 匹配语句缺少 '{'")
	val arms = mutableListOf<MatchArm>()
	while (!cursor.match(RBRACE)) {
		if (cursor.match(ELSE)) {
			val elseStatements = parseElseStatements()
			val end = cursor.previous.location
			return MatchPatternExpression(subject, arms, start span end, elseStatements)
		}
		var guard: Expression? = null
		var alias: Identifier? = null
		val patterns = buildList {
			do {
				when {
					cursor.match(NOT, IS) -> {
						val type = parseTypeReference()
						this += IsMatchPattern(true, type)
					}
					
					cursor.match(IS) -> {
						val type = parseTypeReference()
						this += IsMatchPattern(false, type)
					}
					
					cursor.match(NOT, IN) -> {
						val expression = parseExpressionChain()
						this += InMatchPattern(true, expression)
					}
					
					cursor.match(IN) -> {
						val expression = parseExpressionChain()
						this += InMatchPattern(false, expression)
					}
					
					else -> {
						val expression = parseExpressionChain()
						this += ExpressionMatchPattern(expression)
					}
				}
				if (cursor.match(AS)) {
					alias = parseIdentifier(IdentifierTarget.MATCH_AS)
					if (cursor.match(IF)) {
						guard = parseExpressionChain()
					}
					cursor.expect(ARROW, "match 匹配分支缺少 '->'")
					break
				}
				if (cursor.match(IF)) {
					guard = parseExpressionChain()
					cursor.expect(ARROW, "match 匹配分支缺少 '->'")
					break
				}
				if (cursor.current.kind != ARROW) {
					cursor.expect(COMMA, "match 匹配分支缺少 ','")
				}
			} while (!cursor.match(ARROW))
		}
		val body = if (cursor.match(LBRACE)) {
			parseStatements()
		} else {
			listOf(parseStatement())
		}
		val end = cursor.previous.location
		arms += MatchArm(
			patterns = patterns,
			alias = alias,
			guard = guard,
			body = body,
			location = start span end
		)
	}
	if (arms.isEmpty()) {
		syntaxError("match 不允许没有匹配分支", cursor.previous)
	}
	val end = cursor.previous.location
	return MatchPatternExpression(
		subject = subject,
		arms = arms,
		location = start span end
	)
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseMatchConditionExpression(): MatchConditionExpression {
	val start = cursor.offset(-2).location
	val cases = mutableListOf<MatchCase>()
	while (!cursor.match(RBRACE)) {
		if (cursor.match(ELSE)) {
			val elseStatements = parseElseStatements()
			val end = cursor.previous.location
			return MatchConditionExpression(cases, start span end, elseStatements)
		}
		val condition = parseExpressionChain()
		val start = condition.location
		cursor.expect(ARROW, "match 条件分支缺少 '->'")
		val body = if (cursor.match(LBRACE)) {
			parseStatements()
		} else {
			listOf(parseStatement())
		}
		val end = cursor.previous.location
		cases += MatchCase(condition, body, start span end)
	}
	if (cases.isEmpty()) {
		syntaxError("match 不允许没有条件分支", cursor.previous)
	}
	val end = cursor.previous.location
	return MatchConditionExpression(cases, start span end)
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseElseStatements(): List<Statement> {
	cursor.expect(ARROW, "else 缺少 '->'")
	val elseStatements = if (cursor.match(LBRACE)) {
		parseStatements()
	} else {
		listOf(parseStatement())
	}
	cursor.expect(RBRACE, "match 表达式缺少 '}'")
	return elseStatements
}