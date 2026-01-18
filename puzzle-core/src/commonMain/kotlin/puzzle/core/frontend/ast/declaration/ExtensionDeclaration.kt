package puzzle.core.frontend.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.frontend.ast.AnnotationCall
import puzzle.core.frontend.ast.Modifier
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.ast.parameter.DeclarationContextSpec
import puzzle.core.frontend.ast.parameter.TypeSpec
import puzzle.core.frontend.ast.type.NamedType
import puzzle.core.frontend.ast.type.SuperTypeReference
import puzzle.core.frontend.ast.type.TypeReference
import puzzle.core.frontend.model.SourceLocation

@Serializable
class ExtensionDeclaration(
	val alias: Identifier,
	val extendedType: TypeReference,
	val modifiers: List<Modifier>,
	val superTypes: List<SuperTypeReference>,
	val withTypes: List<NamedType>,
	val typeSpec: TypeSpec?,
	val contextSpec: DeclarationContextSpec?,
	val annotationCalls: List<AnnotationCall>,
	val members: List<TopLevelAllowedDeclaration>,
	override val location: SourceLocation,
) : TopLevelAllowedDeclaration