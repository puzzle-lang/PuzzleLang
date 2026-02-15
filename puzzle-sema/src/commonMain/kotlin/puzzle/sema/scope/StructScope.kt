package puzzle.sema.scope

import puzzle.core.context.FileContext
import puzzle.sema.checker.checkDuplicate
import puzzle.sema.symbol.PzlSymbol
import puzzle.sema.symbol.StructSymbol
import puzzle.sema.util.isAnonymous

class StructScope(
	override val parent: PzlScope<FileContext>,
	override val owner: StructSymbol,
) : PzlScope<FileContext>, InitContainer {
	
	private val symbolsByName = mutableMapOf<String?, MutableList<PzlSymbol>>()
	
	override val orderedSymbols = mutableListOf<PzlSymbol>()
	
	override val initBlocks = mutableListOf<BlockScope>()
	
	context(_: FileContext)
	override fun declare(symbol: PzlSymbol) {
		val name = symbol.name
		if (name != null && name.isAnonymous) return
		val sameNameSymbols = symbolsByName.getOrPut(name?.value) { mutableListOf() }
		sameNameSymbols.checkDuplicate(symbol)
		sameNameSymbols += symbol
		orderedSymbols += symbol
	}
	
	override fun lookup(name: String?): List<PzlSymbol> {
		return symbolsByName[name] ?: parent.lookup(name)
	}
	
	override fun lookupLocal(name: String?): List<PzlSymbol> {
		return symbolsByName[name].orEmpty()
	}
}