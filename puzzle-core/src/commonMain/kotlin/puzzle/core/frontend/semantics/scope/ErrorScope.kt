package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.semantics.symbol.ErrorSymbol
import puzzle.core.frontend.semantics.symbol.PzlSymbol

class ErrorScope(
	override val parent: PzlScope<FileContext>,
	override val owner: ErrorSymbol,
) : PzlScope<FileContext> {
	
	private val symbolsMap = mutableMapOf<String, PzlSymbol>()
	
	override val orderedSymbols = mutableListOf<PzlSymbol>()
	
	context(_: FileContext)
	override fun declare(symbol: PzlSymbol) {
		val name = symbol.name!!
		symbolsMap[name.value] = symbol
		orderedSymbols += symbol
	}
	
	override fun lookup(name: String?): List<PzlSymbol> {
		return symbolsMap[name]?.let { listOf(it) } ?: emptyList()
	}
}