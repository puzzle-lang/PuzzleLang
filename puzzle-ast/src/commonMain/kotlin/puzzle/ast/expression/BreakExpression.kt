package puzzle.ast.expression

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import puzzle.core.location.SourceLocation

@Serializable
class BreakExpression(
	override val label: Identifier?,
	val expression: Expression?,
	override val location: SourceLocation,
) : JumpExpression {
	
	@Transient
	override val type = "break"
}