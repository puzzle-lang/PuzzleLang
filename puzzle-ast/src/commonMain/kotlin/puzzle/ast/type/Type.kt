package puzzle.ast.type

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import puzzle.ast.PzlAstNode
import puzzle.ast.argument.TypeArgument
import puzzle.ast.declaration.ReturnSpec
import puzzle.ast.parameter.LambdaContextSpec
import puzzle.ast.parameter.LambdaParameter
import puzzle.diagnostic.source.SourceLocation

@Serializable
sealed interface Type : PzlAstNode

@Serializable
class NamedType(
	@Contextual
	val segments: List<String>,
	override val location: SourceLocation,
	val typeArguments: List<TypeArgument> = emptyList(),
) : Type

@Serializable
class LambdaType(
	val extension: TypeReference?,
	val contextSpec: LambdaContextSpec?,
	val parameters: List<LambdaParameter>,
	val returnSpec: ReturnSpec,
	override val location: SourceLocation,
) : Type