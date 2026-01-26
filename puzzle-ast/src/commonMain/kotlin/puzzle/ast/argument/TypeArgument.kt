package puzzle.ast.argument

import kotlinx.serialization.Serializable
import puzzle.ast.PzlAstNode
import puzzle.ast.expression.Identifier
import puzzle.ast.type.TypeReference
import puzzle.base.location.SourceLocation
import puzzle.base.location.span

@Serializable
class TypeArgument(
	val name: Identifier?,
	val type: TypeReference,
	override val location: SourceLocation =
		if (name != null) name.location span type.location else type.location,
) : PzlAstNode