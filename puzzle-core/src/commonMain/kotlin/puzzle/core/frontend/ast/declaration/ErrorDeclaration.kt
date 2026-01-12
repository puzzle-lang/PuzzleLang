package puzzle.core.frontend.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.ast.AnnotationCall
import puzzle.core.frontend.ast.DocComment
import puzzle.core.frontend.ast.Modifier
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.ast.parameter.Parameter
import puzzle.core.frontend.ast.parameter.TypeSpec

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