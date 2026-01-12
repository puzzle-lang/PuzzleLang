package puzzle.core.frontend.ast.expression

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import puzzle.core.frontend.ast.AstNode

@Serializable
sealed interface Expression : AstNode

@Serializable
sealed interface JumpExpression : Expression {
	
	val label: Identifier?
	
	@Transient
	val type: String
}