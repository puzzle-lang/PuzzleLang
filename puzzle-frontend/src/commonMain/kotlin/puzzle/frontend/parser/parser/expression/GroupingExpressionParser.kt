package puzzle.frontend.parser.parser.expression

import puzzle.ast.expression.GroupingExpression
import puzzle.core.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.token.kinds.BracketKind.End.RPAREN

context(_: FileContext, cursor: PzlTokenCursor)
fun parseGroupingExpression(): GroupingExpression {
	val expression = parseExpressionChain()
	cursor.expect(RPAREN, "'(' 必须由 ')' 结束")
	return GroupingExpression(expression)
}