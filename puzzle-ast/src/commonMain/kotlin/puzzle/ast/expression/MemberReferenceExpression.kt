package puzzle.ast.expression

import kotlinx.serialization.Serializable
import puzzle.base.location.SourceLocation

@Serializable
class MemberReferenceExpression(
	val receiver: Expression?,
	val name: Expression,
	override val location: SourceLocation,
) : Expression, CompoundAssignable