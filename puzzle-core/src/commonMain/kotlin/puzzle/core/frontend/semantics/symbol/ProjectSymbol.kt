package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.semantics.scope.RootScope

class ProjectSymbol(
	override val name: String,
	override val owner: RootScope,
) : Symbol {
	
	override val kind = SymbolKind.PROJECT
	
	override val node = null
	
	override val visibility = null
}