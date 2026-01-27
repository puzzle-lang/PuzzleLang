package puzzle.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.location.SourceLocation

@Serializable
sealed interface ContextualExpression : Expression

@Serializable
class SuperExpression(
	override val location: SourceLocation,
) : ContextualExpression

@Serializable
class ThisExpression(
	override val location: SourceLocation,
) : ContextualExpression, CompoundAssignable