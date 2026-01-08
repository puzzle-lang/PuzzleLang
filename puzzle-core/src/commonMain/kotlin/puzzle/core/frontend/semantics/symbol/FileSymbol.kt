package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.AstFile
import puzzle.core.frontend.semantics.scope.ModuleScope

class FileSymbol(
	override val name: String,
	override val node: AstFile,
) : Symbol<ModuleScope> {
	
	var currentOwner: ModuleScope? = null
	override val owner: ModuleScope
		get() = currentOwner!!
	
	override val kind = SymbolKind.FILE
	
	override val visibility = null
}