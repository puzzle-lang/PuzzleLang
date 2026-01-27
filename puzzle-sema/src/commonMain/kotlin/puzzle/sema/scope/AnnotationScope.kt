package puzzle.sema.scope

import puzzle.core.context.FileContext
import puzzle.sema.checker.checkDuplicate
import puzzle.sema.symbol.AnnotationSymbol
import puzzle.sema.symbol.PzlSymbol

class AnnotationScope(
	override val parent: PzlScope<FileContext>,
	override val owner: AnnotationSymbol,
) : PzlScope<FileContext> {
	
	override val symbolsByName = mutableMapOf<String?, MutableList<PzlSymbol>>()
	
	override val orderedSymbols = mutableListOf<PzlSymbol>()
	
	context(_: FileContext)
	override fun declare(symbol: PzlSymbol) {
		val name = symbol.name!!
		val sameNameSymbols = symbolsByName.getOrPut(name.value) { mutableListOf() }
		sameNameSymbols.checkDuplicate(symbol)
		sameNameSymbols += symbol
		orderedSymbols += symbol
	}
	
	override fun lookupLocal(name: String?): List<PzlSymbol> {
		return symbolsByName[name].orEmpty()
	}
	
	override fun lookup(name: String?): List<PzlSymbol> {
		return symbolsByName[name] ?: parent.lookup(name)
	}
}