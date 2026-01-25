package puzzle.ast.expression

import kotlinx.serialization.Serializable
import puzzle.diagnostic.source.SourceLocation
import puzzle.diagnostic.source.span

@Serializable
class ElvisExpression(
	val left: Expression,
	val right: Expression,
	override val location: SourceLocation = left.location span right.location,
) : Expression, CompoundAssignable