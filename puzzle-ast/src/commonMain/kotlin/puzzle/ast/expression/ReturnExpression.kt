package puzzle.ast.expression

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import puzzle.diagnostic.source.SourceLocation

@Serializable
class ReturnExpression(
	override val label: Identifier?,
	val expression: Expression?,
	override val location: SourceLocation,
) : JumpExpression {
	
	@Transient
	override val type = "return"
}