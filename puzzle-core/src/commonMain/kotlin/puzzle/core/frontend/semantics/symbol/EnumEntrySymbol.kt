package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.EnumEntry
import puzzle.core.frontend.semantics.scope.EnumEntryScope
import puzzle.core.frontend.semantics.scope.PzlScope

class EnumEntrySymbol(
	override val name: String,
	override val owner: PzlScope,
	override val node: EnumEntry,
) : PzlSymbol {
	
	override val kind = PzlSymbolKind.ENUM_ENTRY
	
	override val visibility = null
	
	override var scope: EnumEntryScope? = null
}