package puzzle.frontend.parser.dispatcher.expression

import puzzle.ast.expression.AsExpression
import puzzle.ast.expression.Expression
import puzzle.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.parseAsExpression
import puzzle.frontend.parser.syntaxError
import puzzle.token.kinds.SymbolKind.QUESTION

object AsExpressionDispatcher : ExpressionDispatcher<AsExpression> {
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): AsExpression {
		if (left == null) {
			val isSafe = cursor.check(QUESTION)
			syntaxError(
				message = "${if (isSafe) "as?" else "as"} 前未解析到表达式",
				token = cursor.offset(if (isSafe) -2 else -1)
			)
		}
		return parseAsExpression(left)
	}
}