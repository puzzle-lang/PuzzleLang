package puzzle.ast.type

import kotlinx.serialization.Serializable
import puzzle.ast.PzlAstNode
import puzzle.base.location.SourceLocation

@Serializable
class ErrorsSpec(
	val errorTypes: List<NamedType>,
	override val location: SourceLocation,
) : PzlAstNode