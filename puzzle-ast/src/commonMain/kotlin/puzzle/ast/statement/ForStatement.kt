package puzzle.ast.statement

import kotlinx.serialization.Serializable
import puzzle.base.location.SourceLocation
import puzzle.ast.PzlAstNode
import puzzle.ast.expression.Expression
import puzzle.ast.expression.Identifier
import puzzle.ast.parameter.ParameterReference

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