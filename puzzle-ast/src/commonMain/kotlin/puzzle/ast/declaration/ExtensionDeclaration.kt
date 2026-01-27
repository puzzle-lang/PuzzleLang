package puzzle.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.ast.AnnotationCall
import puzzle.ast.Modifier
import puzzle.ast.expression.Identifier
import puzzle.ast.parameter.DeclarationContextSpec
import puzzle.ast.parameter.TypeSpec
import puzzle.ast.type.NamedType
import puzzle.ast.type.SuperTypeReference
import puzzle.ast.type.TypeReference
import puzzle.core.location.SourceLocation

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