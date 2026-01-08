package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.semantics.symbol.Symbol

class BlockScope(
	override val parent: Scope<*>?,
	override val owner: Symbol<*>?,
) : Scope<BlockScope>() {
	
	private val symbolsMap = mutableMapOf<String, MutableList<Symbol<BlockScope>>>()
	
	private var cached: List<Symbol<BlockScope>>? = null
	
	override val symbols: Collection<Symbol<BlockScope>>
		get() = cached ?: symbolsMap.values.flatten().also { cached = it }
	
	override fun declare(symbol: Symbol<BlockScope>) {
		val symbols = symbolsMap.getOrPut(symbol.name) { mutableListOf() }
		symbols += symbol
		cached = null
	}
	
	override fun lookup(name: String): List<Symbol<*>> {
		return symbolsMap[name] ?: parent?.lookup(name) ?: emptyList()
	}
}