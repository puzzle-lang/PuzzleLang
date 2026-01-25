package puzzle.ast.parameter

import kotlinx.serialization.Serializable
import puzzle.ast.PzlAstNode
import puzzle.ast.expression.Identifier
import puzzle.ast.type.TypeReference
import puzzle.diagnostic.source.SourceLocation
import puzzle.diagnostic.source.span

@Serializable
sealed interface ContextSpec<out R : ContextReceiver> : PzlAstNode {
	
	val receivers: List<R>
	
	val isPropagate: Boolean
}

@Serializable
sealed interface ContextReceiver : PzlAstNode {
	
	val type: TypeReference
}

@Serializable
class DeclarationContextSpec(
	override val receivers: List<DeclarationContextReceiver>,
	override val isPropagate: Boolean,
	override val location: SourceLocation,
) : ContextSpec<DeclarationContextReceiver>

@Serializable
class DeclarationContextReceiver(
	val name: Identifier,
	override val type: TypeReference,
	override val location: SourceLocation = name.location span type.location,
) : ContextReceiver

@Serializable
class LambdaContextSpec(
	override val receivers: List<LambdaContextReceiver>,
	override val isPropagate: Boolean,
	override val location: SourceLocation,
) : ContextSpec<LambdaContextReceiver>

@Serializable
class LambdaContextReceiver(
	override val type: TypeReference,
	val typeExpansion: TypeExpansion?,
	override val location: SourceLocation,
) : ContextReceiver