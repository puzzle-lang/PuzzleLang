package puzzle.builtin.builder

import puzzle.ast.AnnotationCall
import puzzle.ast.Modifier
import puzzle.ast.argument.TypeArgument
import puzzle.ast.expression.Identifier
import puzzle.ast.parameter.Quantifier
import puzzle.ast.parameter.QuantifierKind
import puzzle.core.location.SourceLocation
import puzzle.token.kinds.ModifierKind

@PzlBuiltinDsl
class BuiltinParameterBuilder {
	
	val typeArguments: List<TypeArgument>
		field = mutableListOf()
	
	var quantifier: Quantifier? = null
		private set
	
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
	
	fun quantifier(quantifier: QuantifierKind) {
		if (this.quantifier != null) {
			error("quantifier 不可重复配置")
		}
		this.quantifier = Quantifier(
			kind = quantifier,
			location = SourceLocation.Builtin
		)
	}
	
	fun modifier(modifier: ModifierKind) {
		modifiers += getModifier(modifier)
	}
	
	fun annotationCall(type: String) {
		annotationCalls += getAnnotationCall(type)
	}
}