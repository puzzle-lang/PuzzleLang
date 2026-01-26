package puzzle.ast.expression

import kotlinx.serialization.Serializable
import puzzle.ast.Operator
import puzzle.base.location.SourceLocation
import puzzle.base.location.span

@Serializable
class PrefixUnaryExpression(
	val operator: Operator,
	val expression: Expression,
	override val location: SourceLocation = operator.location span expression.location,
) : Expression