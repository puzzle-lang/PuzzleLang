package puzzle.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.ast.AnnotationCall
import puzzle.ast.DocComment
import puzzle.ast.Modifier
import puzzle.ast.expression.Identifier
import puzzle.ast.parameter.DeclarationContextSpec
import puzzle.ast.parameter.Parameter
import puzzle.ast.parameter.TypeSpec
import puzzle.ast.type.NamedType
import puzzle.ast.type.SuperType
import puzzle.diagnostic.source.SourceLocation

@Serializable
class ClassDeclaration(
	val name: Identifier,
	val docComment: DocComment?,
	val modifiers: List<Modifier>,
	val primaryCtorAnnotationCalls: List<AnnotationCall>,
	val primaryCtorModifiers: List<Modifier>,
	val parameters: List<Parameter>,
	val typeSpec: TypeSpec?,
	val contextSpec: DeclarationContextSpec?,
	val annotationCalls: List<AnnotationCall>,
	val superTypes: List<SuperType>,
	val withTypes: List<NamedType>,
	val ctors: List<CtorDeclaration>,
	val inits: List<InitDeclaration>,
	val members: List<TopLevelAllowedDeclaration>,
	override val location: SourceLocation,
) : TopLevelAllowedDeclaration