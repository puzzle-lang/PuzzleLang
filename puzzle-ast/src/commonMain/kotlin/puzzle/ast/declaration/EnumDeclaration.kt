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
import puzzle.ast.type.SuperTypeReference
import puzzle.diagnostic.source.SourceLocation

@Serializable
class EnumDeclaration(
	val name: Identifier,
	val docComment: DocComment?,
	val modifiers: List<Modifier>,
	val parameters: List<Parameter>,
	val entries: List<EnumEntry>,
	val superTypes: List<SuperTypeReference>,
	val withTypes: List<NamedType>,
	val typeSpec: TypeSpec?,
	val contextSpec: DeclarationContextSpec?,
	val annotationCalls: List<AnnotationCall>,
	override val location: SourceLocation,
	val inits: List<InitDeclaration> = emptyList(),
	val ctors: List<CtorDeclaration> = emptyList(),
	val members: List<TopLevelAllowedDeclaration> = emptyList(),
) : TopLevelAllowedDeclaration

@Serializable
class EnumEntry(
	val name: Identifier,
	val inits: List<InitDeclaration>,
	val members: List<TopLevelAllowedDeclaration>,
	override val location: SourceLocation,
) : Declaration