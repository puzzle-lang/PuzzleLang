package puzzle.frontend.parser.parser.argument

import puzzle.ast.argument.TypeArgument
import puzzle.core.context.FileContext
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.IdentifierTarget
import puzzle.frontend.parser.parser.expression.parseIdentifier
import puzzle.frontend.parser.parser.type.parseTypeReference
import puzzle.token.kinds.AssignmentKind.ASSIGN
import puzzle.token.kinds.OperatorKind.GT
import puzzle.token.kinds.OperatorKind.LT
import puzzle.token.kinds.SeparatorKind.COMMA

context(_: FileContext, cursor: PzlTokenCursor)
fun parseTypeArguments(): List<TypeArgument> {
	if (!cursor.match(LT)) {
		return emptyList()
	}
	val arguments = mutableListOf<TypeArgument>()
	do {
		arguments += parseTypeArgument()
		if (!cursor.check(GT)) {
			cursor.expect(COMMA, "泛型参数列表缺少 ','")
		}
	} while (!cursor.match(GT))
	return arguments
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseTypeArgument(): TypeArgument {
	val name = if (cursor.offsetOrNull(offset = 1)?.kind == ASSIGN) {
		parseIdentifier(IdentifierTarget.TYPE_ARGUMENT).also {
			cursor.advance()
		}
	} else null
	val type = parseTypeReference(allowLambda = true)
	return TypeArgument(name, type)
}