package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.semantics.symbol.PackageSymbol
import puzzle.core.frontend.semantics.symbol.PzlSymbol

class PackageScope(
	override val parent: PzlScope?,
	override val owner: PackageSymbol,
) : PzlScope {
	
	private val symbolsMap = mutableMapOf<String?, MutableList<PzlSymbol>>()
	
	private var cached: List<PzlSymbol>? = null
	
	override val symbols: Collection<PzlSymbol>
		get() = cached ?: symbolsMap.values.flatten().also { cached = it }
	
	override fun declare(symbol: PzlSymbol) {
		val symbols = symbolsMap.getOrPut(symbol.name) { mutableListOf() }
		symbols += symbol
		cached = null
	}
	
	override fun lookup(name: String): List<PzlSymbol> {
		return symbolsMap[name] ?: emptyList()
	}
}