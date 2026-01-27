package puzzle.builtin.builder

import puzzle.ast.AnnotationCall
import puzzle.ast.Modifier
import puzzle.ast.argument.TypeArgument
import puzzle.ast.expression.Identifier
import puzzle.core.location.SourceLocation
import puzzle.token.kinds.ModifierKind

@PzlBuiltinDsl
class BuiltinPropertyBuilder {
	
	val typeArguments: List<TypeArgument>
		field = mutableListOf()
	
	val modifiers: List<Modifier>
		field = mutableListOf()
	
	val annotationCalls: List<AnnotationCall>
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
	
	fun modifier(modifier: ModifierKind) {
		modifiers += getModifier(modifier)
	}
	
	fun modifiers(modifier1: ModifierKind, modifier2: ModifierKind, vararg modifiers: ModifierKind) {
		this.modifiers += listOf(modifier1, modifier2, *modifiers).map { getModifier(it) }
	}
	
	fun annotationCall(type: String) {
		annotationCalls += getAnnotationCall(type)
	}
}