package puzzle.sema.scope

import puzzle.core.context.ProjectContext
import puzzle.sema.symbol.ProjectSymbol
import puzzle.sema.symbol.PzlSymbol

class ProjectScope(
	override val parent: RootScope,
	override val owner: ProjectSymbol,
) : PzlScope<ProjectContext> {
	
	override val symbolsByName = mutableMapOf<String?, MutableList<PzlSymbol>>()
	
	override val orderedSymbols = mutableListOf<PzlSymbol>()
	
	context(_: ProjectContext)
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