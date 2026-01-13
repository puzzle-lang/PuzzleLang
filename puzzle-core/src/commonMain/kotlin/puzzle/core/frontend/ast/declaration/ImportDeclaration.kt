package puzzle.core.frontend.ast.declaration

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import puzzle.core.frontend.ast.PzlAstNode
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.model.SourceLocation

@Serializable
class ImportDeclaration(
	@Contextual
	val segments: List<String>,
	val alias: Identifier?,
	val scope: ImportScope,
	override val location: SourceLocation,
) : PzlAstNode

enum class ImportScope {
	SINGLE,
	WILDCARD,
	RECURSIVE
}