package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.semantics.scope.ModuleScope

class ModuleSymbol(
	override val name: String,
	override val owner: ModuleScope,
) : Symbol {
	
	override val kind = SymbolKind.MODULE
	
	override val node = null
	
	override val visibility = null
}