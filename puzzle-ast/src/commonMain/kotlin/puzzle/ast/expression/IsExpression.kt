package puzzle.ast.expression

import kotlinx.serialization.Serializable
import puzzle.ast.type.TypeReference
import puzzle.diagnostic.source.SourceLocation
import puzzle.diagnostic.source.span

@Serializable
class IsExpression(
	val expression: Expression,
	val type: TypeReference,
	val negated: Boolean,
	override val location: SourceLocation = expression.location span type.location,
) : Expression, CompoundAssignable