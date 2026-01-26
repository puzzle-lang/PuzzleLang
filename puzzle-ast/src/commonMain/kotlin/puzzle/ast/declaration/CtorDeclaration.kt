package puzzle.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.ast.AnnotationCall
import puzzle.ast.DocComment
import puzzle.ast.Modifier
import puzzle.ast.expression.Identifier
import puzzle.ast.parameter.Parameter
import puzzle.ast.statement.Statement
import puzzle.base.location.SourceLocation

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