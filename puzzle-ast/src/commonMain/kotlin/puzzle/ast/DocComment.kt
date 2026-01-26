package puzzle.ast

import kotlinx.serialization.Serializable
import puzzle.base.location.SourceLocation

@Serializable
class DocComment(
	val value: String,
	override val location: SourceLocation,
) : PzlAstNode