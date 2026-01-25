package puzzle.ast.expression

import kotlinx.serialization.Serializable
import puzzle.ast.PzlAstNode
import puzzle.diagnostic.source.SourceLocation
import puzzle.diagnostic.source.span

@Serializable
sealed interface InvokeExpression : Expression {
	
	val callee: Expression
	
	val arguments: List<Argument>
}

@Serializable
class CallExpression(
	override val callee: Expression,
	override val arguments: List<Argument>,
	override val location: SourceLocation,
) : InvokeExpression, CompoundAssignable

@Serializable
class IndexAccessExpression(
	override val callee: Expression,
	override val arguments: List<Argument>,
	override val location: SourceLocation,
) : InvokeExpression, DirectAssignable, CompoundAssignable

@Serializable
class Argument(
	val name: Identifier?,
	val expression: Expression,
	override val location: SourceLocation = if (name != null) name.location span expression.location else expression.location,
) : PzlAstNode