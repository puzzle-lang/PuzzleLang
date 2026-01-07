package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.semantics.symbol.EnumEntrySymbol
import puzzle.core.frontend.semantics.symbol.Symbol

class EnumEntryScope(
	override val parent: Scope<*>?,
	override val owner: EnumEntrySymbol<*>,
) : Scope<EnumEntryScope> {
	
	private val symbolsMap = mutableMapOf<String, MutableList<Symbol<EnumEntryScope>>>()
	
	private var cached: List<Symbol<EnumEntryScope>>? = null
	
	override val symbols: Collection<Symbol<EnumEntryScope>>
		get() = cached ?: symbolsMap.values.flatten().also { cached = it }
	
	override fun declare(symbol: Symbol<EnumEntryScope>) {
		val symbols = symbolsMap.getOrPut(symbol.name) { mutableListOf() }
		symbols += symbol
		cached = null
	}
	
	override fun lookupLocal(name: String): Symbol<*>? {
		return symbolsMap[name]?.firstOrNull()
			?: parent?.lookupLocal(name)
	}
}