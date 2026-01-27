package puzzle.ast.expression

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import puzzle.core.location.SourceLocation

@Serializable
class ContinueExpression(
	override val label: Identifier?,
	override val location: SourceLocation,
) : JumpExpression {
	
	@Transient
	override val type = "continue"
}