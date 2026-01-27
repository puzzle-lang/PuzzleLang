package puzzle.builtin.builder

import puzzle.ast.argument.TypeArgument
import puzzle.ast.expression.Identifier
import puzzle.core.location.SourceLocation

@PzlBuiltinDsl
class BuiltinTypeArgumentBuilder {
	
	val typeArguments: List<TypeArgument>
		field = mutableListOf()
	
	fun typeArgument(
		type: String,
		isNullable: Boolean = false,
		name: String? = null,
		builder: BuiltinTypeArgumentBuilder.() -> Unit = {},
	) {
		typeArguments += TypeArgument(
			name = name?.let {
				Identifier(
					value = it,
					location = SourceLocation.Builtin
				)
			},
			type = getTypeReference(
				type = type,
				isNullable = isNullable,
				typeArguments = BuiltinTypeArgumentBuilder().apply(builder).typeArguments
			)
		)
	}
}