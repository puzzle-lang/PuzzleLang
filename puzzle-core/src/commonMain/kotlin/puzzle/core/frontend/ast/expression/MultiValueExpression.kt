package puzzle.core.frontend.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation

@Serializable
class MultiValueExpression(
	val expressions: List<Expression>,
	override val location: SourceLocation,
) : Expression