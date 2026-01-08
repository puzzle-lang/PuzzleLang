package puzzle.core.frontend.ast.builtin.builder

import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.ast.parameter.*
import puzzle.core.frontend.ast.type.TypeReference
import puzzle.core.frontend.model.SourceLocation

@PzlBuiltinDsl
class BuiltinTypeParameterBuilder {
	
	val typeParameters: List<TypeParameter>
		field = mutableListOf()
	
	fun typeParameter(
		name: String,
		variance: VarianceKind? = null,
		typeExpansion: TypeExpansionKind? = null,
		boundBuilder: BuiltinBoundBuilder.() -> Unit = {},
	) {
		typeParameters += TypeParameter(
			name = Identifier(
				value = name,
				location = SourceLocation.Builtin,
			),
			variance = variance?.let { kind ->
				Variance(
					kind = kind,
					location = SourceLocation.Builtin
				)
			},
			bounds = BuiltinBoundBuilder().apply(boundBuilder).bounds,
			typeExpansion = typeExpansion?.let { kind ->
				TypeExpansion(
					kind = kind,
					location = SourceLocation.Builtin
				)
			},
			defaultType = null,
			location = SourceLocation.Builtin
		)
	}
}

@PzlBuiltinDsl
class BuiltinBoundBuilder {
	
	val bounds: List<TypeReference>
		field = mutableListOf()
	
	fun bound(
		type: String,
		isNullable: Boolean = false,
		builder: BuiltinTypeArgumentBuilder.() -> Unit = {},
	) {
		bounds += getTypeReference(
			type = type,
			isNullable = isNullable,
			typeArguments = BuiltinTypeArgumentBuilder().apply(builder).typeArguments
		)
	}
}