package puzzle.ast.statement

import kotlinx.serialization.Serializable
import puzzle.base.location.SourceLocation
import puzzle.ast.PzlAstNode
import puzzle.ast.expression.Expression
import puzzle.ast.expression.Identifier
import puzzle.ast.type.TypeReference

@Serializable
class VariableDeclarationStatement(
	val variableSpec: VariableSpec,
	val initializer: Expression?,
	override val location: SourceLocation,
) : Statement

@Serializable
sealed interface VariableSpec : PzlAstNode

@Serializable
class SingleVariableSpec(
	val variable: Variable,
	override val location: SourceLocation = variable.location,
) : VariableSpec

@Serializable
class DestructureVariableSpec(
	val variables: List<Variable>,
	override val location: SourceLocation,
) : VariableSpec

@Serializable
class Variable(
	val isMutable: Boolean,
	val name: Identifier,
	val type: TypeReference?,
	override val location: SourceLocation,
) : PzlAstNode