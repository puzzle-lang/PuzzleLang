package puzzle.ast.expression

import kotlinx.serialization.Serializable
import puzzle.base.location.SourceLocation
import puzzle.ast.statement.Statement

@Serializable
class LoopExpression(
	val label: Identifier?,
	val body: List<Statement>,
	override val location: SourceLocation,
) : Expression, CompoundAssignable