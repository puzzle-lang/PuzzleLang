package puzzle.core.frontend.ast.parameter

import kotlinx.serialization.Serializable
import puzzle.core.frontend.ast.PzlAstNode
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.ast.type.TypeReference
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.token.kinds.ContextualKind
import puzzle.core.frontend.token.kinds.OperatorKind
import puzzle.core.frontend.token.kinds.PzlTokenKind

@Serializable
class TypeSpec(
	val reified: Boolean,
	val parameters: List<TypeParameter>,
	override val location: SourceLocation,
) : PzlAstNode

@Serializable
class TypeParameter(
	val name: Identifier,
	val variance: Variance?,
	val bounds: List<TypeReference>,
	val typeExpansion: TypeExpansion?,
	val defaultType: TypeReference?,
	override val location: SourceLocation,
) : PzlAstNode

@Serializable
class Variance(
	val kind: VarianceKind,
	override val location: SourceLocation,
) : PzlAstNode

@Serializable
enum class VarianceKind(
	val kind: PzlTokenKind,
) {
	
	IN(OperatorKind.IN),
	
	OUT(ContextualKind.OUT)
}