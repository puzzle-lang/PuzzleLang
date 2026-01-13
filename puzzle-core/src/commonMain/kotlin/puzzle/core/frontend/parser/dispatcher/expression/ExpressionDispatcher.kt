package puzzle.core.frontend.parser.dispatcher.expression

import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor

sealed interface ExpressionDispatcher<out E : Expression> {
	
	context(_: FileContext, _: PzlTokenCursor)
	fun parse(left: Expression?): E
}