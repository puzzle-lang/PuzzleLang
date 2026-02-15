package puzzle.sema.scope

import puzzle.core.context.ModuleContext
import puzzle.sema.symbol.PackageSymbol
import puzzle.sema.symbol.PzlSymbol

class PackageScope(
	override val parent: PzlScope<*>,
	override val owner: PackageSymbol,
) : PzlScope<ModuleContext> {
	
	private val symbolsByName = mutableMapOf<String?, MutableList<PzlSymbol>>()
	
	override val orderedSymbols = mutableListOf<PzlSymbol>()
	
	context(_: ModuleContext)
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