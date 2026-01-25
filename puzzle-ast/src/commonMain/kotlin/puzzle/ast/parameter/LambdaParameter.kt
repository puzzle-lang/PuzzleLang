package puzzle.ast.parameter

import kotlinx.serialization.Serializable
import puzzle.diagnostic.source.SourceLocation
import puzzle.ast.PzlAstNode
import puzzle.ast.type.TypeReference
import puzzle.ast.expression.Identifier

@Serializable
class LambdaParameter(
	val name: Identifier?,
	val type: TypeReference,
	override val location: SourceLocation,
) : PzlAstNode