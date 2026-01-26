package puzzle.frontend.parser.dispatcher.expression

import puzzle.ast.expression.Expression
import puzzle.ast.expression.OracleExpression
import puzzle.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.parseOracleExpression
import puzzle.frontend.parser.syntaxError

object OracleExpressionDispatcher : ExpressionDispatcher<OracleExpression> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): OracleExpression {
		if (left == null) {
			syntaxError("'|:' 前未解析到表达式", cursor.previous)
		}
		return parseOracleExpression(left)
	}
}