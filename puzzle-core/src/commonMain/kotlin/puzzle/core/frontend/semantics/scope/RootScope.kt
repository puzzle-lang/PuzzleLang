package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.semantics.symbol.Symbol

class RootScope : Scope<RootScope> {
	
	override val parent: Scope<*>? = null
	override val owner: Symbol<*>? = null
	
	private val symbolsMap = mutableMapOf<String, MutableList<Symbol<RootScope>>>()
	
	private var cached: List<Symbol<RootScope>>? = null
	
	override val symbols: Collection<Symbol<RootScope>>
		get() = cached ?: symbolsMap.values.flatten().also { cached = it }
	
	override fun declare(symbol: Symbol<RootScope>) {
		val symbols = symbolsMap.getOrPut(symbol.name) { mutableListOf() }
		symbols += symbol
		cached = null
	}
	
	override fun lookup(name: String): List<Symbol<*>> {
		return symbolsMap[name] ?: emptyList()
	}
}