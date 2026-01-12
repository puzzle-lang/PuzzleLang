package puzzle.core.frontend.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.frontend.ast.AstNode
import puzzle.core.frontend.ast.statement.Statement
import puzzle.core.frontend.ast.type.TypeReference
import puzzle.core.frontend.model.SourceLocation

@Serializable
sealed interface MatchExpression : Expression, CompoundAssignable

@Serializable
class MatchConditionExpression(
	val cases: List<MatchCase>,
	override val location: SourceLocation,
	val elseBody: List<Statement>? = null,
) : MatchExpression

@Serializable
class MatchCase(
	val condition: Expression,
	val body: List<Statement>,
	override val location: SourceLocation,
) : AstNode

@Serializable
class MatchPatternExpression(
	val subject: Expression,
	val arms: List<MatchArm>,
	override val location: SourceLocation,
	val elseBody: List<Statement>? = null,
) : MatchExpression

@Serializable
class MatchArm(
	val patterns: List<MatchPattern>,
	val alias: Identifier?,
	val guard: Expression?,
	val body: List<Statement>,
	override val location: SourceLocation,
) : AstNode

@Serializable
sealed interface MatchPattern

@Serializable
class ExpressionMatchPattern(
	val expression: Expression,
) : MatchPattern

@Serializable
class IsMatchPattern(
	val type: TypeReference,
) : MatchPattern