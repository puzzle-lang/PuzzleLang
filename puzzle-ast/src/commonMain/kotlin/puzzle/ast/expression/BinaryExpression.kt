package puzzle.ast.expression

import kotlinx.serialization.Serializable
import puzzle.ast.Operator
import puzzle.core.location.SourceLocation
import puzzle.core.location.span

@Serializable
class BinaryExpression(
	val left: Expression,
	val operator: Operator,
	val right: Expression,
	override val location: SourceLocation = left.location span right.location,
) : Expression, CompoundAssignable