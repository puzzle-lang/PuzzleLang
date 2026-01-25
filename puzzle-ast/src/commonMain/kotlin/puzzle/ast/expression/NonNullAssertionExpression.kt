package puzzle.ast.expression

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import puzzle.diagnostic.source.SourceLocation

@Serializable
class NonNullAssertionExpression(
	val receiver: Expression,
	override val location: SourceLocation,
) : Expression, CompoundAssignableProxy {
	
	@Transient
	override val inner = receiver
}