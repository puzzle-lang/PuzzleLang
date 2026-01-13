package puzzle.core.frontend.parser.dispatcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.expression.ElvisExpression
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.parseElvisExpression

object ElvisExpressionDispatcher : ExpressionDispatcher<ElvisExpression> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): ElvisExpression {
		if (left == null) {
			syntaxError("'?:' 前未解析到表达式", cursor.previous)
		}
		return parseElvisExpression(left)
	}
}