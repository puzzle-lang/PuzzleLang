package puzzle.ast.statement

import kotlinx.serialization.Serializable
import puzzle.base.location.SourceLocation
import puzzle.ast.expression.Argument
import puzzle.ast.type.NamedType

@Serializable
class InitStatement(
	val type: NamedType,
	val arguments: List<Argument>,
	val isSafe: Boolean,
	override val location: SourceLocation,
) : Statement