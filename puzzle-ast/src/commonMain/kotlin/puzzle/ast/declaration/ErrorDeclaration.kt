package puzzle.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.ast.AnnotationCall
import puzzle.ast.DocComment
import puzzle.ast.Modifier
import puzzle.ast.expression.Identifier
import puzzle.ast.parameter.Parameter
import puzzle.ast.parameter.TypeSpec
import puzzle.base.location.SourceLocation

@Serializable
class ErrorDeclaration(
	val name: Identifier,
	val docComment: DocComment?,
	val modifiers: List<Modifier>,
	val parameters: List<Parameter>,
	val typeSpec: TypeSpec?,
	val annotationCalls: List<AnnotationCall>,
	override val location: SourceLocation,
) : TopLevelAllowedDeclaration