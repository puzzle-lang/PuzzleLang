package puzzle.ast.statement

import kotlinx.serialization.Serializable
import puzzle.diagnostic.source.SourceLocation
import puzzle.ast.Assignment
import puzzle.ast.expression.Expression

@Serializable
sealed interface AssignmentStatement : Statement {
	
	val target: Expression
	
	val assignment: Assignment
	
	val value: Expression
}

@Serializable
class DirectAssignmentStatement(
	override val target: Expression,
	override val assignment: Assignment,
	override val value: Expression,
	override val location: SourceLocation,
) : AssignmentStatement

@Serializable
class CompoundAssignmentStatement(
	override val target: Expression,
	override val assignment: Assignment,
	override val value: Expression,
	override val location: SourceLocation,
) : AssignmentStatement