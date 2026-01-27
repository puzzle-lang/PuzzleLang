package puzzle.frontend.parser.dispatcher.expression

import puzzle.ast.expression.Expression
import puzzle.core.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor

sealed interface ExpressionDispatcher<out E : Expression> {
	
	context(_: FileContext, _: PzlTokenCursor)
	fun parse(left: Expression?): E
}