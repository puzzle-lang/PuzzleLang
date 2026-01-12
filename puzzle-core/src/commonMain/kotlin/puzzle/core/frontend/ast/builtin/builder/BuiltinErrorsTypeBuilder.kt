package puzzle.core.frontend.ast.builtin.builder

import puzzle.core.frontend.ast.type.NamedType
import puzzle.core.frontend.model.SourceLocation

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