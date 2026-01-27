package puzzle.frontend.parser.dispatcher.expression

import puzzle.ast.expression.Expression
import puzzle.ast.expression.IsExpression
import puzzle.core.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.parseIsExpression
import puzzle.frontend.parser.syntaxError
import puzzle.token.kinds.OperatorKind.NOT

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