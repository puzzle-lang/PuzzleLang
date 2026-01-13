package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.semantics.symbol.ObjectSymbol
import puzzle.core.frontend.semantics.symbol.PzlSymbol

class ObjectScope(
	override val parent: PzlScope?,
	override val owner: ObjectSymbol,
) : PzlScope, InitContainer {
	
	private val symbolsMap = mutableMapOf<String?, MutableList<PzlSymbol>>()
	
	private var cached: List<PzlSymbol>? = null
	
	override val symbols: Collection<PzlSymbol>
		get() = cached ?: symbolsMap.values.flatten().also { cached = it }
	
	override val initBlocks = mutableListOf<BlockScope>()
	
	override fun declare(symbol: PzlSymbol) {
		val symbols = symbolsMap.getOrPut(symbol.name) { mutableListOf() }
		symbols += symbol
		cached = null
	}
	
	override fun lookup(name: String): List<PzlSymbol> {
		return symbolsMap[name] ?: parent?.lookup(name) ?: emptyList()
	}
}