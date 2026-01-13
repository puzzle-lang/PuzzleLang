package puzzle.core.frontend.parser.parser.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.equalsLine
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.dispatcher.expression.*
import puzzle.core.frontend.token.kinds.AccessKind
import puzzle.core.frontend.token.kinds.AccessKind.DOUBLE_COLON
import puzzle.core.frontend.token.kinds.AccessorKind.GET
import puzzle.core.frontend.token.kinds.AccessorKind.SET
import puzzle.core.frontend.token.kinds.AssignmentKind
import puzzle.core.frontend.token.kinds.BracketKind.End.*
import puzzle.core.frontend.token.kinds.BracketKind.Start.*
import puzzle.core.frontend.token.kinds.ContextualKind.SUPER
import puzzle.core.frontend.token.kinds.ContextualKind.THIS
import puzzle.core.frontend.token.kinds.ControlFlowKind.*
import puzzle.core.frontend.token.kinds.JumpKind.*
import puzzle.core.frontend.token.kinds.LiteralKind
import puzzle.core.frontend.token.kinds.MetaKind.EOF
import puzzle.core.frontend.token.kinds.OperatorKind.*
import puzzle.core.frontend.token.kinds.SeparatorKind.COMMA
import puzzle.core.frontend.token.kinds.SeparatorKind.SEMICOLON
import puzzle.core.frontend.token.kinds.SymbolTokenKind.*
import puzzle.core.frontend.token.kinds.TypeOperatorKind.AS
import puzzle.core.frontend.token.kinds.TypeOperatorKind.IS

context(_: FileContext, cursor: PzlTokenCursor)
fun parseExpression(left: Expression? = null): Expression {
	val dispatcher = parseExpressionDispatcher(left)
	return dispatcher.parse(left)
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseExpressionDispatcher(left: Expression?): ExpressionDispatcher<*> {
	val kind = cursor.current.kind
	var dispatcher = when (kind) {
		LPAREN -> GroupingExpressionDispatcher
		DOUBLE_COLON -> MemberReferenceExpressionDispatcher
		in PrefixUnaryExpressionDispatcher.kinds if (left == null || (kind != PLUS && kind != MINUS)) -> PrefixUnaryExpressionDispatcher
		is LiteralKind -> LiteralExpressionDispatcher
		in BinaryExpressionDispatcher.operators -> BinaryExpressionDispatcher
		ELVIS -> ElvisExpressionDispatcher
		ORACLE -> OracleExpressionDispatcher
		RETURN -> ReturnExpressionDispatcher
		BREAK -> BreakExpressionDispatcher
		CONTINUE -> ContinueExpressionDispatcher
		QUESTION -> TernaryExpressionDispatcher
		IS -> IsExpressionDispatcher
		NOT if cursor.nextOrNull?.kind == IS -> {
			cursor.advance()
			IsExpressionDispatcher
		}
		
		AS -> AsExpressionDispatcher
		IF -> IfExpressionDispatcher
		MATCH -> MatchExpressionDispatcher
		LOOP -> LoopExpressionDispatcher
		LBRACE -> LambdaExpressionDispatcher
		LBRACKET -> MultiValueExpressionDispatcher
		THIS, SUPER -> PostfixExpressionDispatcher
		
		else -> null
	}
	if (dispatcher != null) {
		cursor.advance()
		return dispatcher
	}
	
	if (!cursor.matchIdentifier()) {
		syntaxError("不支持的表达式", cursor.current)
	}
	if (!cursor.match(AT)) {
		return PostfixExpressionDispatcher
	}
	dispatcher = when (cursor.current.kind) {
		LOOP -> LoopExpressionDispatcher
		LBRACE -> LambdaExpressionDispatcher
		else -> syntaxError("不支持的表达式", cursor.current)
	}
	cursor.advance()
	return dispatcher
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