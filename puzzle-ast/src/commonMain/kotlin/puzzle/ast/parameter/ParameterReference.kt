package puzzle.ast.parameter

import kotlinx.serialization.Serializable
import puzzle.ast.PzlAstNode
import puzzle.ast.expression.Identifier
import puzzle.ast.type.TypeReference
import puzzle.core.location.SourceLocation
import puzzle.core.location.span

@Serializable
class ParameterReference(
	val name: Identifier,
	val type: TypeReference? = null,
	override val location: SourceLocation =
		if (type == null) name.location else name.location span type.location,
) : PzlAstNode