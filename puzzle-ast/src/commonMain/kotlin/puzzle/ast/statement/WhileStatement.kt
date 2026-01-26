package puzzle.ast.statement

import kotlinx.serialization.Serializable
import puzzle.base.location.SourceLocation
import puzzle.ast.expression.Expression
import puzzle.ast.expression.Identifier

@Serializable
class WhileStatement(
	val label: Identifier?,
	val condition: Expression,
	val kind: WhileKind,
	val body: List<Statement>,
	override val location: SourceLocation,
) : Statement

enum class WhileKind {
	WHILE,
	DO_WHILE
}