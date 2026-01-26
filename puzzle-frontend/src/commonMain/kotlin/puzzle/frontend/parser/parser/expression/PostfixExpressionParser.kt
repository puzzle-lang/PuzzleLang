package puzzle.frontend.parser.parser.expression

import puzzle.ast.Operator
import puzzle.ast.expression.*
import puzzle.context.FileContext
import puzzle.base.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.util.equalsLine
import puzzle.token.kinds.AccessKind.*
import puzzle.token.kinds.BracketKind.Start.LBRACKET
import puzzle.token.kinds.BracketKind.Start.LPAREN
import puzzle.token.kinds.ContextualKind.SUPER
import puzzle.token.kinds.ContextualKind.THIS
import puzzle.token.kinds.OperatorKind
import puzzle.token.kinds.OperatorKind.*

context(_: FileContext, cursor: PzlTokenCursor)
fun parsePostfixExpression(
	expression: Expression = parseInitExpression(),
): Expression {
	var expression = expression
	while (true) {
		expression = when {
			cursor.match(DOT) -> parseMemberAccessExpression(expression, isSafe = false)
			cursor.match(QUESTION_DOT) -> parseMemberAccessExpression(expression, isSafe = true)
			cursor.match { it.kind == DOUBLE_COLON && it.equalsLine(cursor.previous) } -> parseMemberReferenceExpression(expression)
			cursor.match { it.kind == LPAREN && it.equalsLine(cursor.previous) } -> parseCallExpression(expression)
			cursor.matchLambda() -> parseTrailingClosureCallExpression(expression)
			cursor.match { it.kind == LBRACKET && it.equalsLine(cursor.previous) } -> parseIndexAccessExpression(expression)
			cursor.match { it.kind == NOT && it.equalsLine(cursor.previous) } -> parseNonNullAssertionExpression(expression)
			cursor.match { (it.kind == DOUBLE_PLUS || it.kind == DOUBLE_MINUS) && it.equalsLine(cursor.previous) } -> parsePostfixUnaryExpression(expression)
			else -> break
		}
	}
	return expression
}

context(_: FileContext, cursor: PzlTokenCursor)
fun parseInitExpression(): Expression {
	val token = cursor.previous
	return when (token.kind) {
		THIS -> ThisExpression(token.location)
		SUPER -> SuperExpression(token.location)
		else -> token.toIdentifier()
	}
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseMemberAccessExpression(receiver: Expression, isSafe: Boolean): MemberAccessExpression {
	val name = parseIdentifier(IdentifierTarget.ACCESS_OPERATOR)
	return MemberAccessExpression(receiver, name, isSafe)
}

context(_: FileContext, cursor: PzlTokenCursor)
fun parseMemberReferenceExpression(receiver: Expression?): MemberReferenceExpression {
	val start = receiver?.location ?: cursor.previous.location
	val name = parseIdentifier(IdentifierTarget.ACCESS_OPERATOR)
	val end = cursor.previous.location
	return MemberReferenceExpression(receiver, name, start span end)
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseCallExpression(callee: Expression): CallExpression {
	val start = callee.location
	val arguments = parseArguments(ArgumentTarget.CALL)
	val end = cursor.previous.location
	return CallExpression(callee, arguments, start span end)
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseTrailingClosureCallExpression(callee: Expression): CallExpression {
	val expression = parseLambdaExpression()
	val start = expression.location
	val arguments = listOf(Argument(null, expression))
	val end = cursor.previous.location
	return CallExpression(callee, arguments, start span end)
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseIndexAccessExpression(callee: Expression): IndexAccessExpression {
	val start = callee.location
	val arguments = parseArguments(ArgumentTarget.INDEX_ACCESS)
	val end = cursor.previous.location
	return IndexAccessExpression(callee, arguments, start span end)
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseNonNullAssertionExpression(receiver: Expression): NonNullAssertionExpression {
	val end = cursor.previous.location
	return NonNullAssertionExpression(receiver, receiver.location span end)
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parsePostfixUnaryExpression(expression: Expression): PostfixUnaryExpression {
	val token = cursor.previous
	val operator = Operator(token.kind as OperatorKind, token.location)
	return PostfixUnaryExpression(expression, operator)
}