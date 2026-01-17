package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.model.ModuleContext
import puzzle.core.frontend.semantics.symbol.PackageSymbol
import puzzle.core.frontend.semantics.symbol.PzlSymbol

class PackageScope(
	override val parent: PzlScope<*>,
	override val owner: PackageSymbol,
) : PzlScope<ModuleContext> {
	
	private val symbolsMap = mutableMapOf<Identifier?, MutableList<PzlSymbol>>()
	
	override val orderedSymbols = mutableListOf<PzlSymbol>()
	
	context(_: ModuleContext)
	override fun declare(symbol: PzlSymbol) {
		val sameNameSymbols = symbolsMap.getOrPut(symbol.name) { mutableListOf() }
		sameNameSymbols += symbol
		orderedSymbols += symbol
	}
	
	override fun lookup(name: Identifier?): List<PzlSymbol> {
		return symbolsMap[name] ?: emptyList()
	}
}