package puzzle.ast

import kotlinx.serialization.Serializable
import puzzle.diagnostic.source.SourceLocation

@Serializable
class DocComment(
	val value: String,
	override val location: SourceLocation,
) : PzlAstNode