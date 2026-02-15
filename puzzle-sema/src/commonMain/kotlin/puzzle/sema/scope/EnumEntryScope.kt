package puzzle.sema.scope

import puzzle.core.context.FileContext
import puzzle.sema.checker.checkDuplicate
import puzzle.sema.symbol.EnumEntrySymbol
import puzzle.sema.symbol.PzlSymbol
import puzzle.sema.util.isAnonymous

class EnumEntryScope(
	override val parent: EnumScope,
	override val owner: EnumEntrySymbol,
) : PzlScope<FileContext>, InitContainer {
	
	private val symbolsByName = mutableMapOf<String?, MutableList<PzlSymbol>>()
	
	override val orderedSymbols = mutableListOf<PzlSymbol>()
	
	override val initBlocks = mutableListOf<BlockScope>()
	
	context(_: FileContext)
	override fun declare(symbol: PzlSymbol) {
		val name = symbol.name!!
		if (name.isAnonymous) return
		val symbols = symbolsByName.getOrPut(name.value) { mutableListOf() }
		symbols.checkDuplicate(symbol)
		symbols += symbol
		orderedSymbols += symbols
	}
	
	override fun lookup(name: String?): List<PzlSymbol> {
		return symbolsByName[name] ?: parent.lookup(name)
	}
}