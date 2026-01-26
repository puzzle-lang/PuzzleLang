package puzzle.sema.scope

import puzzle.context.FileContext
import puzzle.sema.checker.checkDuplicate
import puzzle.sema.symbol.PzlSymbol
import puzzle.sema.util.isAnonymous

class BlockScope(
	override val parent: PzlScope<FileContext>,
	override val owner: PzlSymbol? = null,
) : PzlScope<FileContext> {
	
	override val symbolsByName = mutableMapOf<String?, MutableList<PzlSymbol>>()
	
	override val orderedSymbols = mutableListOf<PzlSymbol>()
	
	context(_: FileContext)
	override fun declare(symbol: PzlSymbol) {
		val name = symbol.name!!
		if (name.isAnonymous) return
		val sameNameSymbols = symbolsByName.getOrPut(name.value) { mutableListOf() }
		sameNameSymbols.checkDuplicate(symbol)
		sameNameSymbols += symbol
		orderedSymbols += symbol
	}
	
	override fun lookup(name: String?): List<PzlSymbol> {
		return symbolsByName[name] ?: parent.lookup(name)
	}
}