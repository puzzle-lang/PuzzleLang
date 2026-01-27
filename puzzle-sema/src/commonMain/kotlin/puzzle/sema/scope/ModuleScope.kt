package puzzle.sema.scope

import puzzle.core.context.ModuleContext
import puzzle.sema.symbol.ModuleSymbol
import puzzle.sema.symbol.PzlSymbol

class ModuleScope(
	override val parent: ProjectScope,
	override val owner: ModuleSymbol?,
) : PzlScope<ModuleContext> {
	
	override val symbolsByName = mutableMapOf<String?, MutableList<PzlSymbol>>()
	
	override val orderedSymbols = mutableListOf<PzlSymbol>()
	
	context(_: ModuleContext)
	override fun declare(symbol: PzlSymbol) {
		val name = symbol.name!!
		val symbols = symbolsByName.getOrPut(name.value) { mutableListOf() }
		symbols += symbol
		orderedSymbols += symbol
	}
	
	override fun lookupLocal(name: String?): List<PzlSymbol> {
		return symbolsByName[name].orEmpty()
	}
}