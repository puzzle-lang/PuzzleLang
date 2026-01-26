package puzzle.ast.expression

import kotlinx.serialization.Serializable
import puzzle.base.location.SourceLocation
import puzzle.base.location.span

@Serializable
class MemberAccessExpression(
	val receiver: Expression,
	val name: Expression,
	val isSafe: Boolean,
	override val location: SourceLocation =
		receiver.location span name.location,
) : Expression, DirectAssignable, CompoundAssignable