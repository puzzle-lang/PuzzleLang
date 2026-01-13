package puzzle.core.frontend.parser.dispatcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.expression.AsExpression
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.parseAsExpression
import puzzle.core.frontend.token.kinds.SymbolTokenKind.QUESTION

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