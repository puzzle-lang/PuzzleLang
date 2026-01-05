package puzzle.core.frontend.parser.parser.parameter.type

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.parameter.TypeSpec
import puzzle.core.frontend.model.PzlContext
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.token.kinds.ContextualKind.REIFIED
import puzzle.core.frontend.token.kinds.ContextualKind.TYPE
import puzzle.core.frontend.token.kinds.OperatorKind.LT

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseTypeSpec(): TypeSpec? {
	return when {
		cursor.match(REIFIED, TYPE) -> parseTypeSpec(true)
		cursor.match(TYPE) -> parseTypeSpec(false)
		else -> null
	}
}

context(_: PzlContext, cursor: PzlTokenCursor)
fun TypeSpec.check(target: TypeTarget) {
	if (!target.allowsType) {
		syntaxError("${target.label}声明不支持泛型", cursor[this.location.start])
	}
	if (!target.allowsVariance) {
		this.parameters.forEach {
			if (it.variance != null) {
				syntaxError("${target.label}声明不支持使用 '${it.variance.kind.kind.value}'", cursor[it.location.start])
			}
		}
	}
}

context(_: PzlContext, cursor: PzlTokenCursor)
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