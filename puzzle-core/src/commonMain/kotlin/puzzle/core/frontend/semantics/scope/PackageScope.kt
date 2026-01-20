package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.model.ModuleContext
import puzzle.core.frontend.semantics.symbol.PackageSymbol
import puzzle.core.frontend.semantics.symbol.PzlSymbol

class PackageScope(
	override val parent: PzlScope<*>,
	override val owner: PackageSymbol,
) : PzlScope<ModuleContext> {
	
	override val symbolsByName = mutableMapOf<String?, MutableList<PzlSymbol>>()
	
	override val orderedSymbols = mutableListOf<PzlSymbol>()
	
	context(_: ModuleContext)
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