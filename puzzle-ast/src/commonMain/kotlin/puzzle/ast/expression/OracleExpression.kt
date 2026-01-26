package puzzle.ast.expression

import kotlinx.serialization.Serializable
import puzzle.base.location.SourceLocation
import puzzle.base.location.span

@Serializable
class OracleExpression(
	val left: Expression,
	val right: Expression,
	override val location: SourceLocation = left.location span right.location,
) : Expression, CompoundAssignable