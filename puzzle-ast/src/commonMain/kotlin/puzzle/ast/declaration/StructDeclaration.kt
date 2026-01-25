package puzzle.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.diagnostic.source.SourceLocation
import puzzle.ast.AnnotationCall
import puzzle.ast.DocComment
import puzzle.ast.Modifier
import puzzle.ast.expression.Identifier
import puzzle.ast.parameter.DeclarationContextSpec
import puzzle.ast.parameter.Parameter
import puzzle.ast.parameter.TypeSpec
import puzzle.ast.type.SuperTypeReference

@Serializable
class StructDeclaration(
	val name: Identifier,
	val docComment: DocComment?,
	val modifiers: List<Modifier>,
	val primaryCtorAnnotationCalls: List<AnnotationCall>,
	val primaryCtorModifiers: List<Modifier>,
	val parameters: List<Parameter>,
	val superTypes: List<SuperTypeReference>,
	val typeSpec: TypeSpec?,
	val contextSpec: DeclarationContextSpec?,
	val annotationCalls: List<AnnotationCall>,
	val inits: List<InitDeclaration>,
	val members: List<TopLevelAllowedDeclaration>,
	override val location: SourceLocation,
) : TopLevelAllowedDeclaration