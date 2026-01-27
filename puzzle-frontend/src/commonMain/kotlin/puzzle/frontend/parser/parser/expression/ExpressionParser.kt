package puzzle.frontend.parser.parser.expression

import puzzle.ast.expression.Expression
import puzzle.core.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.expression.*
import puzzle.frontend.parser.syntaxError
import puzzle.frontend.util.equalsLine
import puzzle.token.kinds.AccessKind
import puzzle.token.kinds.AccessKind.DOUBLE_COLON
import puzzle.token.kinds.AccessorKind.GET
import puzzle.token.kinds.AccessorKind.SET
import puzzle.token.kinds.AssignmentKind
import puzzle.token.kinds.BracketKind.End.*
import puzzle.token.kinds.BracketKind.Start.*
import puzzle.token.kinds.ContextualKind.SUPER
import puzzle.token.kinds.ContextualKind.THIS
import puzzle.token.kinds.ControlFlowKind.*
import puzzle.token.kinds.JumpKind.*
import puzzle.token.kinds.LiteralKind
import puzzle.token.kinds.MetaKind.EOF
import puzzle.token.kinds.OperatorKind.*
import puzzle.token.kinds.SeparatorKind
import puzzle.token.kinds.SeparatorKind.COMMA
import puzzle.token.kinds.SymbolKind.*
import puzzle.token.kinds.TypeOperatorKind.AS
import puzzle.token.kinds.TypeOperatorKind.IS

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
	if (current == SeparatorKind.SEMICOLON) {
		cursor.advance()
		return true
	}
	if (current.kind is AccessKind || current.kind == AND || current.kind == OR) return false
	val previous = cursor.previousOrNull ?: return false
	return !current.equalsLine(previous)
}