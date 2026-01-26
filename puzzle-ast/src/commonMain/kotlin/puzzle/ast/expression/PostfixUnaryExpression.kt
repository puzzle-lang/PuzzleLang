package puzzle.ast.expression

import kotlinx.serialization.Serializable
import puzzle.ast.Operator
import puzzle.base.location.SourceLocation
import puzzle.base.location.span

@Serializable
class PostfixUnaryExpression(
	val expression: Expression,
	val operator: Operator,
	override val location: SourceLocation = expression.location span operator.location,
) : Expression