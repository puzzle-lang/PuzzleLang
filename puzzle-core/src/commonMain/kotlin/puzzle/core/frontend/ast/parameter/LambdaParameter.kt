package puzzle.core.frontend.ast.parameter

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.ast.PzlAstNode
import puzzle.core.frontend.ast.type.TypeReference
import puzzle.core.frontend.ast.expression.Identifier

@Serializable
class LambdaParameter(
	val name: Identifier?,
	val type: TypeReference,
	override val location: SourceLocation,
) : PzlAstNode