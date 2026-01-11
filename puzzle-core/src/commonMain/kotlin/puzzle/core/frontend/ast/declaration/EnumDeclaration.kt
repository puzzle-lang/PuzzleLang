package puzzle.core.frontend.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.frontend.ast.AnnotationCall
import puzzle.core.frontend.ast.DocComment
import puzzle.core.frontend.ast.Modifier
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.ast.parameter.DeclarationContextSpec
import puzzle.core.frontend.ast.parameter.Parameter
import puzzle.core.frontend.ast.parameter.TypeSpec
import puzzle.core.frontend.ast.type.NamedType
import puzzle.core.frontend.ast.type.SuperTypeReference
import puzzle.core.frontend.model.SourceLocation

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