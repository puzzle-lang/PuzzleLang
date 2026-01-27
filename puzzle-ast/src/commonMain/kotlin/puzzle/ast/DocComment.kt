package puzzle.ast

import kotlinx.serialization.Serializable
import puzzle.core.location.SourceLocation

@Serializable
class DocComment(
	val value: String,
	override val location: SourceLocation,
) : PzlAstNode