package puzzle.ast.expression

import kotlinx.serialization.Serializable
import puzzle.base.location.SourceLocation
import puzzle.ast.parameter.ParameterReference
import puzzle.ast.statement.Statement

@Serializable
class LambdaExpression(
	val label: Identifier?,
	val references: List<ParameterReference>,
	val body: List<Statement>,
	override val location: SourceLocation,
) : Expression, CompoundAssignable