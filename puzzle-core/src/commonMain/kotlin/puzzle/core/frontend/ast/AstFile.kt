package puzzle.core.frontend.ast

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import puzzle.core.frontend.ast.declaration.TopLevelAllowedDeclaration
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.util.PathWrapper

@Serializable
class AstFile(
	val name: String,
	@Contextual
	val sourcePath: PathWrapper?,
	val builtin: Boolean,
	val packageDirective: PackageDirective?,
	val importDirectives: List<ImportDirective>,
	val declarations: List<TopLevelAllowedDeclaration>,
	override val location: SourceLocation,
) : PzlAstNode

@Serializable
class PackageDirective(
	@Contextual
	val segments: List<String>,
	override val location: SourceLocation,
) : PzlAstNode

@Serializable
class ImportDirective(
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