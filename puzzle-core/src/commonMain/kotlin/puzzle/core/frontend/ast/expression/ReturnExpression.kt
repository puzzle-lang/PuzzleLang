package puzzle.core.frontend.ast.expression

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import puzzle.core.frontend.model.SourceLocation

@Serializable
class ReturnExpression(
	override val label: Identifier?,
	val expression: Expression?,
	override val location: SourceLocation,
) : JumpExpression {
	
	@Transient
	override val type = "return"
}