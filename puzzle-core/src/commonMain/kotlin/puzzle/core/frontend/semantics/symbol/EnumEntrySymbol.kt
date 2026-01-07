package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.EnumEntry
import puzzle.core.frontend.semantics.scope.Scope

class EnumEntrySymbol<S : Scope<S>>(
	override val name: String,
	override val owner: S,
	override val node: EnumEntry,
) : Symbol<S> {
	
	override val kind = SymbolKind.ENUM_ENTRY
	
	override val visibility = null
}