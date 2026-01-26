package puzzle.sema.scope

import puzzle.context.FileContext
import puzzle.sema.checker.checkDuplicate
import puzzle.sema.symbol.FileSymbol
import puzzle.sema.symbol.PzlSymbol
import puzzle.sema.util.isAnonymous

class FileScope(
	override val owner: FileSymbol,
) : PzlScope<FileContext> {
	
	override lateinit var parent: PzlScope<*>
	
	override val symbolsByName = mutableMapOf<String?, MutableList<PzlSymbol>>()
	
	override val orderedSymbols = mutableListOf<PzlSymbol>()
	
	context(_: FileContext)
	override fun declare(symbol: PzlSymbol) {
		val name = symbol.name!!
		if (name.isAnonymous) return
		val symbols = symbolsByName.getOrPut(name.value) { mutableListOf() }
		symbols.checkDuplicate(symbol)
		symbols += symbol
		orderedSymbols += symbol
	}
	
	override fun lookup(name: String?): List<PzlSymbol> {
		return symbolsByName[name] ?: parent.lookup(name)
	}
}