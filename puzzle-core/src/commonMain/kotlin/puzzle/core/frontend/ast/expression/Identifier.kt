package puzzle.core.frontend.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation

@Serializable
class Identifier(
	val value: String,
	override val location: SourceLocation,
) : Expression, DirectAssignable, CompoundAssignable