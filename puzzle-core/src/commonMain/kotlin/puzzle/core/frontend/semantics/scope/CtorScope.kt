package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.semantics.symbol.CtorSymbol
import puzzle.core.frontend.semantics.symbol.Symbol

class CtorScope(
	override val parent: Scope?,
	override val owner: CtorSymbol,
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