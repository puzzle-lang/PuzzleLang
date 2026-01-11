package puzzle.core.frontend.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.frontend.ast.AnnotationCall
import puzzle.core.frontend.ast.DocComment
import puzzle.core.frontend.ast.Modifier
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.ast.parameter.Parameter
import puzzle.core.frontend.ast.statement.Statement
import puzzle.core.frontend.model.SourceLocation

@Serializable
class CtorDeclaration(
	val name: Identifier?,
	val docComment: DocComment?,
	val parameters: List<Parameter>,
	val modifiers: List<Modifier>,
	val annotationCalls: List<AnnotationCall>,
	val body: List<Statement>,
	override val location: SourceLocation,
) : Declaration