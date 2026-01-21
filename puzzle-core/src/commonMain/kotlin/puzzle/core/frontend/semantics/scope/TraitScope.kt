package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.isAnonymousBinding
import puzzle.core.frontend.semantics.checker.checkDuplicate
import puzzle.core.frontend.semantics.symbol.PzlSymbol
import puzzle.core.frontend.semantics.symbol.TraitSymbol

class TraitScope(
	override val parent: PzlScope<FileContext>,
	override val owner: TraitSymbol,
) : PzlScope<FileContext> {
	
	override val symbolsByName = mutableMapOf<String?, MutableList<PzlSymbol>>()
	
	override val orderedSymbols = mutableListOf<PzlSymbol>()
	
	context(_: FileContext)
	override fun declare(symbol: PzlSymbol) {
		val name = symbol.name
		if (name != null && name.isAnonymousBinding) return
		val sameNameSymbols = symbolsByName.getOrPut(name?.value) { mutableListOf() }
		sameNameSymbols.checkDuplicate(symbol)
		sameNameSymbols += symbol
		orderedSymbols += symbol
	}
	
	override fun lookup(name: String?): List<PzlSymbol> {
		return symbolsByName[name] ?: parent.lookup(name)
	}
	
	override fun lookupLocal(name: String?): List<PzlSymbol> {
		return symbolsByName[name] .orEmpty()
	}
}