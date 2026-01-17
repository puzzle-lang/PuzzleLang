package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.model.RootContext
import puzzle.core.frontend.semantics.symbol.PzlSymbol

class RootScope(
	override val owner: PzlSymbol,
) : PzlScope<RootContext> {
	
	override val parent: PzlScope<*>? = null
	
	private val symbolsMap = mutableMapOf<Identifier, PzlSymbol>()
	
	override val orderedSymbols = mutableListOf<PzlSymbol>()
	
	context(_: RootContext)
	override fun declare(symbol: PzlSymbol) {
		val name = symbol.name!!
		symbolsMap[name] = symbol
		orderedSymbols += symbol
	}
	
	override fun lookup(name: Identifier?): List<PzlSymbol> {
		return symbolsMap[name]?.let { listOf(it) } ?: emptyList()
	}
}