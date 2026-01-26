package puzzle.ast.statement

import kotlinx.serialization.Serializable
import puzzle.ast.expression.Expression
import puzzle.base.location.SourceLocation

@Serializable
class ExpressionStatement(
	val expression: Expression,
	override val location: SourceLocation = expression.location,
) : Statement