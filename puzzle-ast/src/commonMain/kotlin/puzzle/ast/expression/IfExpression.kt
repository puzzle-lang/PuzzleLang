package puzzle.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.location.SourceLocation
import puzzle.ast.statement.Statement

@Serializable
class IfExpression(
	val condition: Expression,
	val thenBody: List<Statement>,
	val elseBody: List<Statement>,
	override val location: SourceLocation,
) : Expression, CompoundAssignable