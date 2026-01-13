package puzzle.core.frontend.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.model.span
import puzzle.core.frontend.ast.PzlAstNode

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