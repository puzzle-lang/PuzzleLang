package puzzle.core.frontend.parser.parser.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.equalsLine
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.dispatcher.expression.ExpressionMatcher
import puzzle.core.frontend.parser.dispatcher.expression.NoPrefixExpressionParser
import puzzle.core.frontend.parser.dispatcher.expression.OptionalPrefixExpressionParser
import puzzle.core.frontend.parser.dispatcher.expression.RequirePrefixExpressionParser
import puzzle.core.frontend.token.kinds.AccessKind
import puzzle.core.frontend.token.kinds.AccessorKind.GET
import puzzle.core.frontend.token.kinds.AccessorKind.SET
import puzzle.core.frontend.token.kinds.AssignmentKind
import puzzle.core.frontend.token.kinds.BracketKind.End.*
import puzzle.core.frontend.token.kinds.ControlFlowKind.*
import puzzle.core.frontend.token.kinds.MetaKind.EOF
import puzzle.core.frontend.token.kinds.OperatorKind.AND
import puzzle.core.frontend.token.kinds.OperatorKind.OR
import puzzle.core.frontend.token.kinds.SeparatorKind.COMMA
import puzzle.core.frontend.token.kinds.SeparatorKind.SEMICOLON
import puzzle.core.frontend.token.kinds.SymbolTokenKind.ARROW
import puzzle.core.frontend.token.kinds.SymbolTokenKind.COLON

context(_: FileContext, cursor: PzlTokenCursor)
fun parseExpression(left: Expression? = null): Expression {
	val matcher = ExpressionMatcher.matchers.find { it.match(left) }
		?: syntaxError("不支持的表达式", cursor.current)
	return when (matcher) {
		is RequirePrefixExpressionParser<*> -> {
			if (left == null) matcher.prefixError()
			matcher.parse(left)
		}
		
		is NoPrefixExpressionParser<*> -> {
			if (left != null) matcher.prefixError()
			matcher.parse()
		}
		
		is OptionalPrefixExpressionParser<*> -> matcher.parse(left)
	}
}

context(_: FileContext, _: PzlTokenCursor)
fun parseExpressionChain(left: Expression? = null): Expression {
	var expression = left
	while (!isAtExpressionEnd() || expression == null) {
		expression = parseExpression(expression)
	}
	return expression
}

context(_: FileContext, _: PzlTokenCursor)
fun tryParseExpressionChain(): Expression? {
	var expression: Expression? = null
	while (!isAtExpressionEnd()) {
		expression = parseExpression(expression)
	}
	return expression
}

private val endKinds = setOf(
	EOF,
	RPAREN, RBRACE, RBRACKET,
	COLON, COMMA, ARROW,
	IF, ELSE,
	WHILE,
	GET, SET
)

context(_: FileContext, cursor: PzlTokenCursor)
private fun isAtExpressionEnd(): Boolean {
	val current = cursor.current
	if (current.kind in endKinds) return true
	if (current.kind is AssignmentKind) return true
	if (current == SEMICOLON) {
		cursor.advance()
		return true
	}
	if (current.kind is AccessKind || current.kind == AND || current.kind == OR) return false
	val previous = cursor.previousOrNull ?: return false
	return !current.equalsLine(previous)
}