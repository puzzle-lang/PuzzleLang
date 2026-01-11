package puzzle.core.frontend.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.model.SourceLocation

@Serializable
class IfStatement(
	val condition: Expression,
	val thenBody: List<Statement>,
	override val location: SourceLocation,
) : Statement