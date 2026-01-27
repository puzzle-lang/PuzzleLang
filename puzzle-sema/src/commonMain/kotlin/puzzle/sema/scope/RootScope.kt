package puzzle.sema.scope

import puzzle.core.context.RootContext
import puzzle.sema.symbol.PzlSymbol

object RootScope : PzlScope<RootContext> {
	
	override val owner: PzlSymbol?
		get() = error("RootScope 没有 owner")
	
	override val parent: PzlScope<*>
		get() = error("RootScope 没有 parent")
	
	override val symbolsByName = mutableMapOf<String?, MutableList<PzlSymbol>>()
	
	override val orderedSymbols = mutableListOf<PzlSymbol>()
	
	context(_: RootContext)
	override fun declare(symbol: PzlSymbol) {
		val name = symbol.name!!
		val sameNameSymbols = symbolsByName.getOrPut(name.value) { mutableListOf() }
		sameNameSymbols += symbol
		orderedSymbols += symbol
	}
	
	override fun lookupLocal(name: String?): List<PzlSymbol> {
		return symbolsByName[name].orEmpty()
	}
}