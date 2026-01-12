package puzzle.core.frontend.ast.expression

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import puzzle.core.frontend.model.SourceLocation

@Serializable
class ContinueExpression(
	override val label: Identifier?,
	override val location: SourceLocation,
) : JumpExpression {
	
	@Transient
	override val type = "continue"
}