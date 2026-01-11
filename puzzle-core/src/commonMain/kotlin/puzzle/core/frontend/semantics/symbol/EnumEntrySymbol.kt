package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.EnumEntry
import puzzle.core.frontend.semantics.scope.Scope

class EnumEntrySymbol(
	override val name: String,
	override val owner: Scope,
	override val node: EnumEntry,
) : Symbol {
	
	override val kind = SymbolKind.ENUM_ENTRY
	
	override val visibility = null
}