package puzzle.ast.expression

import kotlinx.serialization.Serializable
import puzzle.ast.type.TypeReference
import puzzle.core.location.SourceLocation
import puzzle.core.location.span

@Serializable
class AsExpression(
	val expression: Expression,
	val type: TypeReference,
	val isSafe: Boolean,
	override val location: SourceLocation = expression.location span type.location,
) : Expression, CompoundAssignable