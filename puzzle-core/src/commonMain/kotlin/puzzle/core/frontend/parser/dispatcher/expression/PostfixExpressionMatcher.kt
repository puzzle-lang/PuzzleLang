package puzzle.core.frontend.parser.dispatcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.checkIdentifier
import puzzle.core.frontend.parser.parser.expression.parsePostfixExpression
import puzzle.core.frontend.token.kinds.ContextualKind.SUPER
import puzzle.core.frontend.token.kinds.ContextualKind.THIS
import puzzle.core.frontend.token.kinds.SymbolTokenKind.AT

object PostfixExpressionMatcher : ExpressionMatcher, NoPrefixExpressionParser<Expression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return when {
			cursor.checkIdentifier() && cursor.offsetOrNull(1)?.kind != AT -> {
				cursor.advance()
				true
			}
			
			cursor.match { it.kind == THIS || it.kind == SUPER } -> true
			
			else -> false
		}
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun prefixError(): Nothing {
		syntaxError("标识符前不允许是标识符", cursor.previous)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(): Expression {
		return parsePostfixExpression()
	}
}