package puzzle.ast.statement

import kotlinx.serialization.Serializable
import puzzle.ast.expression.Expression
import puzzle.core.location.SourceLocation

@Serializable
class IfStatement(
	val condition: Expression,
	val thenBody: List<Statement>,
	override val location: SourceLocation,
) : Statement