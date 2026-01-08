package puzzle.core.frontend.parser.parser.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.Operator
import puzzle.core.frontend.ast.expression.BinaryExpression
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.token.kinds.Assoc
import puzzle.core.frontend.token.kinds.OperatorKind

context(_: FileContext, cursor: PzlTokenCursor)
fun parseBinaryExpression(left: Expression): BinaryExpression {
	val previous = cursor.previous
	val operator = Operator(previous.kind as OperatorKind, previous.location)
	val right = parseExpression()
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