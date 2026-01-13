package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.semantics.symbol.EnumSymbol
import puzzle.core.frontend.semantics.symbol.PzlSymbol

class EnumScope(
	override val parent: PzlScope?,
	override val owner: EnumSymbol,
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