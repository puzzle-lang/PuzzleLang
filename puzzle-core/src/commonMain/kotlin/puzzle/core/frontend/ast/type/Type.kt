package puzzle.core.frontend.ast.type

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import puzzle.core.frontend.ast.PzlAstNode
import puzzle.core.frontend.ast.argument.TypeArgument
import puzzle.core.frontend.ast.declaration.ReturnSpec
import puzzle.core.frontend.ast.parameter.LambdaContextSpec
import puzzle.core.frontend.ast.parameter.LambdaParameter
import puzzle.core.frontend.model.SourceLocation

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