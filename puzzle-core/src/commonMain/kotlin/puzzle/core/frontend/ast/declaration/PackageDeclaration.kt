package puzzle.core.frontend.ast.declaration

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import puzzle.core.frontend.ast.PzlAstNode
import puzzle.core.frontend.model.SourceLocation

@Serializable
class PackageDeclaration(
	@Contextual
	val segments: List<String>,
	override val location: SourceLocation,
) : PzlAstNode