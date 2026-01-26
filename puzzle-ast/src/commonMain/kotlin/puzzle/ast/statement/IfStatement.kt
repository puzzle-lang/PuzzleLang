package puzzle.ast.statement

import kotlinx.serialization.Serializable
import puzzle.ast.expression.Expression
import puzzle.base.location.SourceLocation

@Serializable
class IfStatement(
	val condition: Expression,
	val thenBody: List<Statement>,
	override val location: SourceLocation,
) : Statement