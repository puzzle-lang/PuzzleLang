package puzzle.ast.parameter

import kotlinx.serialization.Serializable
import puzzle.ast.AnnotationCall
import puzzle.ast.PzlAstNode
import puzzle.ast.Modifier
import puzzle.ast.expression.Expression
import puzzle.ast.expression.Identifier
import puzzle.ast.type.TypeReference
import puzzle.base.location.SourceLocation

@Serializable
class Parameter(
	val name: Identifier,
	val isMutable: Boolean?,
	val modifiers: List<Modifier>,
	val type: TypeReference,
	val annotationCalls: List<AnnotationCall>,
	val quantifier: Quantifier?,
	val defaultExpression: Expression?,
	override val location: SourceLocation,
) : PzlAstNode