@file:Suppress("ClassName")

package puzzle.ast.parameter

import kotlinx.serialization.Serializable
import puzzle.ast.PzlAstNode
import puzzle.core.location.SourceLocation

@Serializable
class Quantifier(
	val kind: QuantifierKind,
	override val location: SourceLocation,
) : PzlAstNode

@Serializable
class TypeExpansion(
	val kind: TypeExpansionKind,
	override val location: SourceLocation,
) : PzlAstNode

@Serializable
sealed interface QuantifierKind {
	
	val allowEmpty: Boolean
}

@Serializable
sealed class VarargKind(
	override val allowEmpty: Boolean,
) : QuantifierKind {
	
	@Serializable
	object ALLOW_EMPTY : VarargKind(true)
	
	@Serializable
	object NOT_EMPTY : VarargKind(false)
}

@Serializable
sealed class TypeExpansionKind(
	override val allowEmpty: Boolean,
) : QuantifierKind {
	
	@Serializable
	object ALLOW_EMPTY : TypeExpansionKind(true)
	
	@Serializable
	object NOT_EMPTY : TypeExpansionKind(false)
}