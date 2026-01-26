package puzzle.ast.parameter

import kotlinx.serialization.Serializable
import puzzle.ast.PzlAstNode
import puzzle.ast.expression.Identifier
import puzzle.ast.type.TypeReference
import puzzle.base.location.SourceLocation
import puzzle.token.kinds.ContextualKind
import puzzle.token.kinds.OperatorKind
import puzzle.token.kinds.PzlTokenKind

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