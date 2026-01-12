package puzzle.core.frontend.parser.parser.parameter.type

import puzzle.core.frontend.ast.parameter.TypeSpec
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.token.kinds.ContextualKind.REIFIED
import puzzle.core.frontend.token.kinds.ContextualKind.TYPE
import puzzle.core.frontend.token.kinds.OperatorKind.LT

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
	cursor.expect(LT, "'type' 后必须跟 '<'")
	val parameters = parseTypeParameters()
	val end = cursor.previous.location
	return TypeSpec(
		reified = reified,
		parameters = parameters,
		location = start span end,
	)
}