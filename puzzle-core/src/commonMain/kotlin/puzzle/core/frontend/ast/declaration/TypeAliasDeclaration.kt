package puzzle.core.frontend.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.ast.DocComment
import puzzle.core.frontend.ast.Modifier
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.ast.parameter.TypeSpec
import puzzle.core.frontend.ast.type.TypeReference

@Serializable
class TypeAliasDeclaration(
	val name: Identifier,
	val docComment: DocComment?,
	val modifiers: List<Modifier>,
	val typeSpec: TypeSpec?,
	val targetType: TypeReference,
	override val location: SourceLocation,
) : TopLevelAllowedDeclaration