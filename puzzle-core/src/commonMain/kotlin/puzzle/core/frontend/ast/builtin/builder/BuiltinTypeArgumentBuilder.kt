package puzzle.core.frontend.ast.builtin.builder

import puzzle.core.frontend.ast.argument.TypeArgument
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.model.SourceLocation

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
					name = it,
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