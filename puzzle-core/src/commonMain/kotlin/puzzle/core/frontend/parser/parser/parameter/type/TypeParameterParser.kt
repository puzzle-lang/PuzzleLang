package puzzle.core.frontend.parser.parser.parameter.type

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.parameter.TypeParameter
import puzzle.core.frontend.ast.parameter.Variance
import puzzle.core.frontend.ast.parameter.VarianceKind
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.IdentifierTarget
import puzzle.core.frontend.parser.parser.expression.parseIdentifier
import puzzle.core.frontend.parser.parser.parameter.parseTypeExpansion
import puzzle.core.frontend.parser.parser.type.parseTypeReference
import puzzle.core.frontend.token.kinds.AssignmentKind.ASSIGN
import puzzle.core.frontend.token.kinds.OperatorKind.BIT_AND
import puzzle.core.frontend.token.kinds.OperatorKind.GT
import puzzle.core.frontend.token.kinds.SeparatorKind.COMMA
import puzzle.core.frontend.token.kinds.SymbolTokenKind.COLON

context(_: FileContext, cursor: PzlTokenCursor)
fun parseTypeParameters(): List<TypeParameter> {
	val parameters = mutableListOf<TypeParameter>()
	do {
		parameters += parseTypeParameter()
		if (!cursor.check(GT)) {
			cursor.expect(COMMA, "泛型参数缺少 ','")
		}
	} while (!cursor.match(GT))
	return parameters
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseTypeParameter(): TypeParameter {
	val start = cursor.current.location
	val variance = parseVariance()
	val name = parseIdentifier(IdentifierTarget.TYPE_PARAMETER)
	val typeExpansion = parseTypeExpansion()
	val bounds = if (cursor.match(COLON)) {
		buildList {
			do {
				this += parseTypeReference()
			} while (cursor.match(BIT_AND))
		}
	} else emptyList()
	val allowNullable = bounds.all { it.isNullable }
	val defaultType = if (cursor.match(ASSIGN)) {
		if (typeExpansion != null) {
			syntaxError("泛型类型展开不允许设置默认值", cursor.previous)
		}
		parseTypeReference(allowNullable = allowNullable)
	} else null
	val end = cursor.previous.location
	return TypeParameter(
		name = name,
		variance = variance,
		bounds = bounds,
		defaultType = defaultType,
		typeExpansion = typeExpansion,
		location = start span end
	)
}

context(cursor: PzlTokenCursor)
private fun parseVariance(): Variance? {
	VarianceKind.entries.forEach { kind ->
		if (cursor.match(kind.kind)) {
			return Variance(kind, cursor.previous.location)
		}
	}
	return null
}