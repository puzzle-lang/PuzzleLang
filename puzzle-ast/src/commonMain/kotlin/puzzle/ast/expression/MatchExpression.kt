package puzzle.ast.expression

import kotlinx.serialization.Serializable
import puzzle.ast.PzlAstNode
import puzzle.ast.statement.Statement
import puzzle.ast.type.TypeReference
import puzzle.diagnostic.source.SourceLocation

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
) : PzlAstNode

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
) : PzlAstNode

@Serializable
sealed interface MatchPattern

@Serializable
class ExpressionMatchPattern(
	val expression: Expression,
) : MatchPattern

@Serializable
class IsMatchPattern(
	val negated: Boolean,
	val type: TypeReference,
) : MatchPattern

@Serializable
class InMatchPattern(
	val negated: Boolean,
	val expression: Expression,
) : MatchPattern