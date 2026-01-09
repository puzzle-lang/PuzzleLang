package puzzle.core.frontend.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.model.SourceLocation

@Serializable
class BreakStatement(
	val label: Identifier?,
	val expression: Expression?,
	override val location: SourceLocation,
) : Statement