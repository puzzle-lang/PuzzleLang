package puzzle.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.base.location.SourceLocation
import puzzle.ast.AnnotationCall
import puzzle.ast.DocComment
import puzzle.ast.Modifier
import puzzle.ast.expression.Identifier
import puzzle.ast.parameter.DeclarationContextSpec
import puzzle.ast.parameter.Parameter
import puzzle.ast.type.NamedType
import puzzle.ast.type.SuperType

@Serializable
class ObjectDeclaration(
	val name: Identifier?,
	val docComment: DocComment?,
	val modifiers: List<Modifier>,
	val primaryCtorAnnotationCalls: List<AnnotationCall>,
	val primaryCtorModifiers: List<Modifier>,
	val parameters: List<Parameter>,
	val superTypes: List<SuperType>,
	val withTypes: List<NamedType>,
	val contextSpec: DeclarationContextSpec?,
	val annotationCalls: List<AnnotationCall>,
	val inits: List<InitDeclaration>,
	val ctors: List<CtorDeclaration>,
	val members: List<TopLevelAllowedDeclaration>,
	override val location: SourceLocation,
) : TopLevelAllowedDeclaration