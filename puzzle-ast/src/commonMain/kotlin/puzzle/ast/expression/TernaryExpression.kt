package puzzle.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.location.SourceLocation
import puzzle.core.location.span

@Serializable
class TernaryExpression(
	val condition: Expression,
	val thenExpression: Expression,
	val elseExpression: Expression,
	override val location: SourceLocation = condition.location span elseExpression.location,
) : Expression, CompoundAssignable