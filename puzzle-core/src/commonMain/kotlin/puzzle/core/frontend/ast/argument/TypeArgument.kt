package puzzle.core.frontend.ast.argument

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.model.span
import puzzle.core.frontend.ast.PzlAstNode
import puzzle.core.frontend.ast.type.TypeReference
import puzzle.core.frontend.ast.expression.Identifier

@Serializable
class TypeArgument(
	val name: Identifier?,
	val type: TypeReference,
	override val location: SourceLocation =
		if (name != null) name.location span type.location else type.location,
) : PzlAstNode