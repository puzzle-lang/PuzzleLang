package puzzle.ast.statement

import kotlinx.serialization.Serializable
import puzzle.ast.expression.Expression
import puzzle.diagnostic.source.SourceLocation

@Serializable
class ExpressionStatement(
	val expression: Expression,
	override val location: SourceLocation = expression.location,
) : Statement