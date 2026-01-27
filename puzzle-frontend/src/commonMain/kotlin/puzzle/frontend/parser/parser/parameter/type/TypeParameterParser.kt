package puzzle.frontend.parser.parser.parameter.type

import puzzle.ast.parameter.TypeParameter
import puzzle.ast.parameter.Variance
import puzzle.ast.parameter.VarianceKind
import puzzle.core.context.FileContext
import puzzle.core.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.IdentifierTarget
import puzzle.frontend.parser.parser.expression.parseIdentifier
import puzzle.frontend.parser.parser.parameter.parseTypeExpansion
import puzzle.frontend.parser.parser.type.parseTypeReference
import puzzle.frontend.parser.syntaxError
import puzzle.token.kinds.AssignmentKind.ASSIGN
import puzzle.token.kinds.OperatorKind.BIT_AND
import puzzle.token.kinds.OperatorKind.GT
import puzzle.token.kinds.SeparatorKind.COMMA
import puzzle.token.kinds.SymbolKind.COLON

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
	return VarianceKind.entries.firstNotNullOfOrNull { kind ->
		if (cursor.match(kind.kind)) {
			Variance(kind, cursor.previous.location)
		} else null
	}
}