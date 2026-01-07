package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.semantics.scope.Scope

class FileSymbol<S : Scope<S>>(
	override val name: String,
	override val owner: S,
) : Symbol<S> {
	
	override val kind = SymbolKind.FILE
	
	override val node = null
	
	override val visibility = null
}