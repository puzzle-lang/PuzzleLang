package puzzle.builtin.builder

import puzzle.ast.type.NamedType
import puzzle.core.location.SourceLocation

@PzlBuiltinDsl
class BuiltinErrorsTypeBuilder {
	
	val errorTypes: List<NamedType>
		field = mutableListOf()
	
	fun errorType(
		type: String,
		builder: BuiltinTypeArgumentBuilder.() -> Unit = {},
	) {
		errorTypes += NamedType(
			segments = type.split('.'),
			typeArguments = BuiltinTypeArgumentBuilder().apply(builder).typeArguments,
			location = SourceLocation.Builtin
		)
	}
}