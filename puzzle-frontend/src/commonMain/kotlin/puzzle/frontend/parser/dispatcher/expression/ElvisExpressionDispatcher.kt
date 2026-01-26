package puzzle.frontend.parser.dispatcher.expression

import puzzle.ast.expression.ElvisExpression
import puzzle.ast.expression.Expression
import puzzle.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.parseElvisExpression
import puzzle.frontend.parser.syntaxError

object ElvisExpressionDispatcher : ExpressionDispatcher<ElvisExpression> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): ElvisExpression {
		if (left == null) {
			syntaxError("'?:' 前未解析到表达式", cursor.previous)
		}
		return parseElvisExpression(left)
	}
}