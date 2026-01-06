package puzzle.core.frontend.ast.builtin.builder

import puzzle.core.frontend.ast.AnnotationCall
import puzzle.core.frontend.ast.Modifier
import puzzle.core.frontend.ast.argument.TypeArgument
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.ast.parameter.Quantifier
import puzzle.core.frontend.ast.parameter.QuantifierKind
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.token.kinds.ModifierKind

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