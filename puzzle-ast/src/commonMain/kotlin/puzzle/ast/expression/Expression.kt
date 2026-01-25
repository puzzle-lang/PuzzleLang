package puzzle.ast.expression

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import puzzle.ast.PzlAstNode

@Serializable
sealed interface Expression : PzlAstNode

@Serializable
sealed interface JumpExpression : Expression {
	
	val label: Identifier?
	
	@Transient
	val type: String
}