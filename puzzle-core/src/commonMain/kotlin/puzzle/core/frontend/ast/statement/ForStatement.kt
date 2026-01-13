package puzzle.core.frontend.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.ast.PzlAstNode
import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.ast.parameter.ParameterReference

@Serializable
class ForStatement(
	val label: Identifier?,
	val pattern: ForPattern,
	val iterable: Expression,
	val body: List<Statement>,
	override val location: SourceLocation,
) : Statement

@Serializable
sealed interface ForPattern : PzlAstNode

@Serializable
class ForValuePattern(
	val reference: ParameterReference,
	override val location: SourceLocation = reference.location,
) : ForPattern

@Serializable
class ForDestructurePattern(
	val references: List<ParameterReference>,
	override val location: SourceLocation,
) : ForPattern