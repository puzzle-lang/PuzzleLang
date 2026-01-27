package puzzle.ast.expression

import kotlinx.serialization.Serializable
import puzzle.ast.Operator
import puzzle.core.location.SourceLocation
import puzzle.core.location.span

@Serializable
class PrefixUnaryExpression(
	val operator: Operator,
	val expression: Expression,
	override val location: SourceLocation = operator.location span expression.location,
) : Expression