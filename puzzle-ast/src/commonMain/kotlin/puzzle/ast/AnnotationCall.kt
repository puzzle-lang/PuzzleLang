package puzzle.ast

import kotlinx.serialization.Serializable
import puzzle.ast.expression.Argument
import puzzle.ast.type.NamedType
import puzzle.base.location.SourceLocation

@Serializable
class AnnotationCall(
	val type: NamedType,
	override val location: SourceLocation,
	val arguments: List<Argument> = emptyList(),
) : PzlAstNode