package puzzle.ast.expression

import kotlinx.serialization.Serializable
import puzzle.ast.Operator
import puzzle.diagnostic.source.SourceLocation
import puzzle.diagnostic.source.span

@Serializable
class BinaryExpression(
	val left: Expression,
	val operator: Operator,
	val right: Expression,
	override val location: SourceLocation = left.location span right.location,
) : Expression, CompoundAssignable