package puzzle.ast.expression

import kotlinx.serialization.Serializable
import puzzle.ast.Operator
import puzzle.base.location.SourceLocation
import puzzle.base.location.span

@Serializable
class BinaryExpression(
	val left: Expression,
	val operator: Operator,
	val right: Expression,
	override val location: SourceLocation = left.location span right.location,
) : Expression, CompoundAssignable