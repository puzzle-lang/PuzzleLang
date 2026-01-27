package puzzle.frontend.parser.parser.statement

import puzzle.ast.Assignment
import puzzle.ast.expression.CompoundAssignable
import puzzle.ast.expression.CompoundAssignableProxy
import puzzle.ast.expression.DirectAssignable
import puzzle.ast.statement.CompoundAssignmentStatement
import puzzle.ast.statement.DirectAssignmentStatement
import puzzle.ast.statement.ExpressionStatement
import puzzle.ast.statement.Statement
import puzzle.core.context.FileContext
import puzzle.core.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.parseExpressionChain
import puzzle.frontend.parser.syntaxError
import puzzle.token.kinds.AssignmentKind

context(_: FileContext, cursor: PzlTokenCursor)
fun parseOtherStatement(): Statement {
	val expression = parseExpressionChain()
	if (cursor.current.kind !is AssignmentKind) {
		return ExpressionStatement(expression)
	}
	cursor.advance()
	val token = cursor.previous
	val kind = token.kind as AssignmentKind
	if (!kind.isCompound) {
		if (expression !is DirectAssignable) {
			syntaxError("不支持使用 '${token.value}' 赋值", token)
		}
		val value = parseExpressionChain()
		val currentKind = cursor.current.kind
		if (currentKind is AssignmentKind) {
			syntaxError(
				message = "语法错误, 不支持使用 '${currentKind.value}' ${if (currentKind.isCompound) "复合赋值" else "赋值"}",
				token = cursor.current
			)
		}
		return DirectAssignmentStatement(
			target = expression,
			assignment = Assignment(kind, token.location),
			value = value,
			location = expression.location span value.location
		)
	}
	var temp = expression
	while (temp is CompoundAssignableProxy) {
		temp = temp.inner
	}
	if (temp !is CompoundAssignable) {
		syntaxError("不支持使用 '${token.value}' 复合赋值", token)
	}
	val value = parseExpressionChain()
	val currentKind = cursor.current.kind
	if (currentKind is AssignmentKind) {
		syntaxError(
			message = "语法错误, 不支持使用 '${currentKind.value}' ${if (currentKind.isCompound) "复合赋值" else "赋值"}",
			token = cursor.current
		)
	}
	return CompoundAssignmentStatement(
		target = expression,
		assignment = Assignment(kind, token.location),
		value = value,
		location = expression.location span value.location
	)
}