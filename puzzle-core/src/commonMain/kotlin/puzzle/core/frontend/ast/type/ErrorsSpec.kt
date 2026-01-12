package puzzle.core.frontend.ast.type

import kotlinx.serialization.Serializable
import puzzle.core.frontend.ast.AstNode
import puzzle.core.frontend.model.SourceLocation

@Serializable
class ErrorsSpec(
	val errorTypes: List<NamedType>,
	override val location: SourceLocation,
) : AstNode