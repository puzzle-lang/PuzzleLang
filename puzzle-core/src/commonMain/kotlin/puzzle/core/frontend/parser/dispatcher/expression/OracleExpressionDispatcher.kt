package puzzle.core.frontend.parser.dispatcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.ast.expression.OracleExpression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.parseOracleExpression

object OracleExpressionDispatcher : ExpressionDispatcher<OracleExpression> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): OracleExpression {
		if (left == null) {
			syntaxError("'|:' 前未解析到表达式", cursor.previous)
		}
		return parseOracleExpression(left)
	}
}