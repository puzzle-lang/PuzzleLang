package puzzle.ast.expression

import kotlinx.serialization.Serializable
import puzzle.diagnostic.source.SourceLocation
import puzzle.diagnostic.source.span

@Serializable
class MemberAccessExpression(
	val receiver: Expression,
	val name: Expression,
	val isSafe: Boolean,
	override val location: SourceLocation =
		receiver.location span name.location,
) : Expression, DirectAssignable, CompoundAssignable