package puzzle.core.frontend.ast

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.ast.expression.Argument
import puzzle.core.frontend.ast.type.NamedType

@Serializable
class AnnotationCall(
	val type: NamedType,
	override val location: SourceLocation,
	val arguments: List<Argument> = emptyList(),
) : PzlAstNode