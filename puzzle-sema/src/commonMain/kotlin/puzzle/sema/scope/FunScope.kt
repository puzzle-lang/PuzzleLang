package puzzle.sema.scope

import puzzle.core.context.FileContext
import puzzle.sema.symbol.FunSymbol
import puzzle.sema.symbol.PzlSymbol
import puzzle.sema.util.isAnonymous

class FunScope(
	override val parent: PzlScope<FileContext>,
	override val owner: FunSymbol,
) : PzlScope<FileContext> {
	
	private val symbolsByName = mutableMapOf<String?, MutableList<PzlSymbol>>()
	
	override val orderedSymbols = mutableListOf<PzlSymbol>()
	
	context(_: FileContext)
	override fun declare(symbol: PzlSymbol) {
		val name = symbol.name!!
		if (name.isAnonymous) return
		val symbols = symbolsByName.getOrPut(name.value) { mutableListOf() }
		symbols += symbol
		orderedSymbols += symbol
	}
	
	override fun lookup(name: String?): List<PzlSymbol> {
		return symbolsByName[name] ?: parent.lookup(name)
	}
}