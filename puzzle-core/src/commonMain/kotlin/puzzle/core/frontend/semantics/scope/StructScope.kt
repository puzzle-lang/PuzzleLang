package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.semantics.symbol.StructSymbol
import puzzle.core.frontend.semantics.symbol.Symbol

class StructScope(
	override val parent: Scope?,
	override val owner: StructSymbol,
) : Scope {
	
	private val symbolsMap = mutableMapOf<String?, MutableList<Symbol>>()
	
	private var cached: List<Symbol>? = null
	
	override val symbols: Collection<Symbol>
		get() = cached ?: symbolsMap.values.flatten().also { cached = it }
	
	override fun declare(symbol: Symbol) {
		val symbols = symbolsMap.getOrPut(symbol.name) { mutableListOf() }
		symbols += symbol
		cached = null
	}
	
	override fun lookup(name: String): List<Symbol> {
		return symbolsMap[name] ?: parent?.lookup(name) ?: emptyList()
	}
}