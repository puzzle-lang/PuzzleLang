package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.semantics.symbol.Symbol
import puzzle.core.frontend.semantics.symbol.TraitSymbol

class TraitScope(
	override val parent: Scope<*>?,
	override val owner: TraitSymbol<*>,
) : Scope<TraitScope> {
	
	private val symbolsMap = mutableMapOf<String, MutableList<Symbol<TraitScope>>>()
	
	private var cached: List<Symbol<TraitScope>>? = null
	
	override val symbols: Collection<Symbol<TraitScope>>
		get() = cached ?: symbolsMap.values.flatten().also { cached = it }
	
	override fun declare(symbol: Symbol<TraitScope>) {
		val symbols = symbolsMap.getOrPut(symbol.name) { mutableListOf() }
		symbols += symbol
		cached = null
	}
	
	override fun lookup(name: String): List<Symbol<*>> {
		return symbolsMap[name] ?: parent?.lookup(name) ?: emptyList()
	}
}