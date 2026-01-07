package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.semantics.scope.Scope

class PropertySymbol<S : Scope<S>>(
	override val name: String,
	override val owner: S,
	override val visibility: Visibility,
) : Symbol<S> {
	
	override val node = null
	
	override val kind = SymbolKind.PROPERTY
}