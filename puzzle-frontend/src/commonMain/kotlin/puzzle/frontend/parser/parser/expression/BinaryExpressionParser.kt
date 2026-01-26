package puzzle.frontend.parser.parser.expression

import puzzle.ast.Operator
import puzzle.ast.expression.BinaryExpression
import puzzle.ast.expression.Expression
import puzzle.ast.expression.JumpExpression
import puzzle.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.syntaxError
import puzzle.token.kinds.Assoc
import puzzle.token.kinds.OperatorKind

context(_: FileContext, cursor: PzlTokenCursor)
fun parseBinaryExpression(left: Expression): BinaryExpression {
	val previous = cursor.previous
	val operator = Operator(previous.kind as OperatorKind, previous.location)
	val right = parseExpression()
	if (right is JumpExpression) {
		syntaxError("二元运算符后不允许使用 ${right.type}", right)
	}
	if (left !is BinaryExpression) {
		return BinaryExpression(left, operator, right)
	}
	val lastPriority = left.operator.kind.priority
	val priority = operator.kind.priority
	return when {
		lastPriority > priority -> BinaryExpression(left, operator, right)
		lastPriority < priority -> exchange(left, operator, right)
		operator.kind.assoc == Assoc.LEFT -> BinaryExpression(left, operator, right)
		operator.kind.assoc == Assoc.RIGHT -> exchange(left, operator, right)
		else -> syntaxError("没有结合性的运算符不允许连续", previous)
	}
}

private fun exchange(
	left: BinaryExpression,
	operator: Operator,
	right: Expression,
): BinaryExpression {
	return BinaryExpression(
		left = left.left,
		operator = left.operator,
		right = BinaryExpression(left.right, operator, right)
	)
}