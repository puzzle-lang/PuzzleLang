package puzzle.builtin.builder

import puzzle.ast.expression.Identifier
import puzzle.ast.parameter.DeclarationContextReceiver
import puzzle.core.location.SourceLocation

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