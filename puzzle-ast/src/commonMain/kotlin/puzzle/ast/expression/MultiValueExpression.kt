package puzzle.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.location.SourceLocation

@Serializable
class MultiValueExpression(
	val expressions: List<Expression>,
	override val location: SourceLocation,
) : Expression