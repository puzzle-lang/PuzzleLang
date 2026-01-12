package puzzle.core.frontend.ast

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import puzzle.core.frontend.ast.declaration.ImportDeclaration
import puzzle.core.frontend.ast.declaration.PackageDeclaration
import puzzle.core.frontend.ast.declaration.TopLevelAllowedDeclaration
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.util.PathWrapper

@Serializable
class AstFile(
	val name: String,
	@Contextual
	val sourcePath: PathWrapper?,
	val builtin: Boolean,
	val packageDeclaration: PackageDeclaration?,
	val importDeclarations: List<ImportDeclaration>,
	val declarations: List<TopLevelAllowedDeclaration>,
	override val location: SourceLocation,
) : AstNode