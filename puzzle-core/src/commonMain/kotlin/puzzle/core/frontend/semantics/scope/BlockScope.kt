package puzzle.core.frontend.semantics.scope

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.semantics.symbol.PzlSymbol

class BlockScope(
	override val parent: FileContextScope?,
	override val owner: PzlSymbol? = null,
) : FileContextScope {
	
	private val symbolsMap = mutableMapOf<String?, MutableList<PzlSymbol>>()
	
	private var cached: List<PzlSymbol>? = null
	
	override val symbols: Collection<PzlSymbol>
		get() = cached ?: symbolsMap.values.flatten().also { cached = it }
	
	context(_: FileContext)
	override fun declare(symbol: PzlSymbol) {
		val symbols = symbolsMap.getOrPut(symbol.name) { mutableListOf() }
		println(symbol.name)
		if (symbols.isNotEmpty()) {
			syntaxError("${symbol.name} 重复的变量声明", symbol.node!!)
		}
		symbols += symbol
		cached = null
	}
	
	override fun lookup(name: String): List<PzlSymbol> {
		return symbolsMap[name] ?: parent?.lookup(name) ?: emptyList()
	}
}