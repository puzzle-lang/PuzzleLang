package puzzle.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.location.SourceLocation
import puzzle.core.location.span

@Serializable
class OracleExpression(
	val left: Expression,
	val right: Expression,
	override val location: SourceLocation = left.location span right.location,
) : Expression, CompoundAssignable