package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.semantics.symbol.AnnotationSymbol
import puzzle.core.frontend.semantics.symbol.PzlSymbol

class AnnotationScope(
	override val parent: PzlScope<FileContext>,
	override val owner: AnnotationSymbol,
) : PzlScope<FileContext> {
	
	private val symbolMap = mutableMapOf<String, PzlSymbol>()
	
	override val orderedSymbols = mutableListOf<PzlSymbol>()
	
	context(_: FileContext)
	override fun declare(symbol: PzlSymbol) {
		val name = symbol.name!!
		symbolMap[name.value] = symbol
		orderedSymbols += symbol
	}
	
	override fun lookup(name: String?): List<PzlSymbol> {
		return symbolMap[name]?.let { listOf(it) } ?: parent.lookup(name)
	}
}