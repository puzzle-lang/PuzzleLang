package puzzle.core.frontend.ast

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation

@Serializable
class DocComment(
	val value: String,
	override val location: SourceLocation,
) : PzlAstNode