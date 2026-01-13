package puzzle.core.frontend.parser.dispatcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.ast.expression.IsExpression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.parseIsExpression
import puzzle.core.frontend.token.kinds.OperatorKind.NOT

object IsExpressionDispatcher : ExpressionDispatcher<IsExpression> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): IsExpression {
		if (left == null) {
			val negated = cursor.offset(-2).kind == NOT
			syntaxError(
				message = "${if (negated) "!is" else "is"} 前未解析到表达式",
				token = cursor.offset(if (negated) -2 else -1)
			)
		}
		return parseIsExpression(left)
	}
}