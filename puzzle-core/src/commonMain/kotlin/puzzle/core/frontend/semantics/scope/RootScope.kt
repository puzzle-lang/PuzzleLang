package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.model.RootContext
import puzzle.core.frontend.semantics.symbol.PzlSymbol

class RootScope(
	override val owner: PzlSymbol,
) : RootContextScope {
	
	override val parent: PzlScope<*>? = null
	
	private val symbolsMap = mutableMapOf<String?, MutableList<PzlSymbol>>()
	
	private var cached: List<PzlSymbol>? = null
	
	override val symbols: Collection<PzlSymbol>
		get() = cached ?: symbolsMap.values.flatten().also { cached = it }
	
	context(_: RootContext)
	override fun declare(symbol: PzlSymbol) {
		val symbols = symbolsMap.getOrPut(symbol.name) { mutableListOf() }
		symbols += symbol
		cached = null
	}
	
	override fun lookup(name: String): List<PzlSymbol> {
		return symbolsMap[name] ?: emptyList()
	}
}