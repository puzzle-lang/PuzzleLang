package puzzle.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.location.SourceLocation
import puzzle.ast.DocComment
import puzzle.ast.Modifier
import puzzle.ast.expression.Identifier
import puzzle.ast.parameter.TypeSpec
import puzzle.ast.type.TypeReference

@Serializable
class TypeAliasDeclaration(
	val name: Identifier,
	val docComment: DocComment?,
	val modifiers: List<Modifier>,
	val typeSpec: TypeSpec?,
	val targetType: TypeReference,
	override val location: SourceLocation,
) : TopLevelAllowedDeclaration