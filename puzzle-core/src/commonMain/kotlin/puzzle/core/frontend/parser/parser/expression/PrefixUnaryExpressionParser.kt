package puzzle.core.frontend.parser.parser.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.Operator
import puzzle.core.frontend.ast.expression.*
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.token.kinds.OperatorKind
import puzzle.core.frontend.token.kinds.OperatorKind.DOUBLE_MINUS
import puzzle.core.frontend.token.kinds.OperatorKind.DOUBLE_PLUS

context(_: FileContext, cursor: PzlTokenCursor)
fun parsePrefixUnaryExpression(): PrefixUnaryExpression {
	val token = cursor.previous
	val expression = parseExpression()
	if (expression is PostfixUnaryExpression) {
		syntaxError("不能在这里使用后缀操作符 '${expression.operator.kind.value}'", expression)
	}
	if (expression is JumpExpression) {
		syntaxError("前缀运算符 '${token.kind.value}' 后不允许使用 ${expression.type}", expression)
	}
	if (
		expression !is Identifier && expression !is MemberAccessExpression &&
		(token.kind == DOUBLE_PLUS || token.kind == DOUBLE_MINUS)
	) {
		syntaxError("不能在这里使用前缀操作符 '${token.value}'", token)
	}
	val operator = Operator(token.kind as OperatorKind, token.location)
	return PrefixUnaryExpression(operator, expression)
}