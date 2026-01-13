package puzzle.core.frontend.ast.parameter

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.model.span
import puzzle.core.frontend.ast.PzlAstNode
import puzzle.core.frontend.ast.type.TypeReference
import puzzle.core.frontend.ast.expression.Identifier

@Serializable
class ParameterReference(
	val name: Identifier,
	val type: TypeReference? = null,
	override val location: SourceLocation = if (type == null) name.location else name.location span type.location,
) : PzlAstNode