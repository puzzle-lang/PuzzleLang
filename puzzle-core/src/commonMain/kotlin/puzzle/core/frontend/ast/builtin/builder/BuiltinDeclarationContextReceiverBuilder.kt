package puzzle.core.frontend.ast.builtin.builder

import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.ast.parameter.DeclarationContextReceiver
import puzzle.core.frontend.model.SourceLocation

@PzlBuiltinDsl
class BuiltinDeclarationContextReceiverBuilder {
	
	val contextReceivers: List<DeclarationContextReceiver>
		field = mutableListOf()
	
	fun contextReceiver(
		name: String,
		type: String,
		isNullable: Boolean = false,
		builder: BuiltinTypeArgumentBuilder.() -> Unit = {},
	) {
		contextReceivers += DeclarationContextReceiver(
			name = Identifier(
				value = name,
				location = SourceLocation.Builtin,
			),
			type = getTypeReference(
				type = type,
				isNullable = isNullable,
				typeArguments = BuiltinTypeArgumentBuilder().apply(builder).typeArguments
			),
			location = SourceLocation.Builtin
		)
	}
}