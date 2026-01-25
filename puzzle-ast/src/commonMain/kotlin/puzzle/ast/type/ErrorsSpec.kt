package puzzle.ast.type

import kotlinx.serialization.Serializable
import puzzle.ast.PzlAstNode
import puzzle.diagnostic.source.SourceLocation

@Serializable
class ErrorsSpec(
	val errorTypes: List<NamedType>,
	override val location: SourceLocation,
) : PzlAstNode