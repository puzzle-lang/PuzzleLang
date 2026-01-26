package puzzle.ast.expression

import kotlinx.serialization.Serializable
import puzzle.base.location.SourceLocation
import puzzle.base.location.span

@Serializable
class TernaryExpression(
	val condition: Expression,
	val thenExpression: Expression,
	val elseExpression: Expression,
	override val location: SourceLocation = condition.location span elseExpression.location,
) : Expression, CompoundAssignable