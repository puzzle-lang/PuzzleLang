package puzzle.ast.expression

import kotlinx.serialization.Serializable
import puzzle.ast.Operator
import puzzle.diagnostic.source.SourceLocation
import puzzle.diagnostic.source.span

@Serializable
class PrefixUnaryExpression(
	val operator: Operator,
	val expression: Expression,
	override val location: SourceLocation = operator.location span expression.location,
) : Expression