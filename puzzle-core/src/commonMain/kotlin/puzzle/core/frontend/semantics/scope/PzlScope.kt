package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.model.Context
import puzzle.core.frontend.semantics.symbol.PzlSymbol

sealed interface PzlScope<CTX : Context> {
	
	val parent: PzlScope<*>?
	
	val owner: PzlSymbol?
	
	val orderedSymbols: List<PzlSymbol>
	
	context(_: CTX)
	fun declare(symbol: PzlSymbol)
	
	fun lookup(name: String?): List<PzlSymbol>
}