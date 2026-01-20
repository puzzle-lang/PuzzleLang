package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.model.RootContext
import puzzle.core.frontend.semantics.symbol.PzlSymbol

class RootScope(
	override val owner: PzlSymbol,
) : PzlScope<RootContext> {
	
	override val parent: PzlScope<*>
		get() = error("Root 作用域没有 parent")
	
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
		return symbolsByName[name] ?: emptyList()
	}
}