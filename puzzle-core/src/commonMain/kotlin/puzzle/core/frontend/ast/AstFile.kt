package puzzle.core.frontend.ast

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import puzzle.core.frontend.ast.declaration.Declaration
import puzzle.core.frontend.ast.declaration.ImportDeclaration
import puzzle.core.frontend.ast.declaration.PackageDeclaration
import puzzle.core.util.PathWrapper

@Serializable
class AstFile(
	val name: String,
	@Contextual
	val sourcePath: PathWrapper?,
	val builtin: Boolean,
	val packageDeclaration: PackageDeclaration?,
	val importDeclarations: List<ImportDeclaration>,
	val declarations: List<Declaration>,
)