package puzzle.frontend.parser.parser.parameter.type

import puzzle.ast.parameter.TypeSpec
import puzzle.context.FileContext
import puzzle.base.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.token.kinds.ContextualKind.REIFIED
import puzzle.token.kinds.ContextualKind.TYPE
import puzzle.token.kinds.OperatorKind

context(_: FileContext, cursor: PzlTokenCursor)
fun parseTypeSpec(): TypeSpec? {
	return when {
		cursor.match(REIFIED, TYPE) -> parseTypeSpec(true)
		cursor.match(TYPE) -> parseTypeSpec(false)
		else -> null
	}
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseTypeSpec(reified: Boolean): TypeSpec {
	val start = cursor.offset(if (reified) -2 else -1).location
	cursor.expect(OperatorKind.LT, "'type' 后必须跟 '<'")
	val parameters = parseTypeParameters()
	val end = cursor.previous.location
	return TypeSpec(
		reified = reified,
		parameters = parameters,
		location = start span end,
	)
}