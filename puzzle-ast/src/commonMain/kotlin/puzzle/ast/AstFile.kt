package puzzle.ast

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import puzzle.ast.declaration.TopLevelAllowedDeclaration
import puzzle.ast.expression.Identifier
import puzzle.core.location.SourceLocation

@Serializable
class AstFile(
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