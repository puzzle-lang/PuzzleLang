package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.model.Context
import puzzle.core.frontend.semantics.symbol.PzlSymbol

sealed interface PzlScope<C : Context> {
	
	val parent: PzlScope<*>?
	
	val owner: PzlSymbol?
	
	val orderedSymbols: List<PzlSymbol>
	
	context(_: C)
	fun declare(symbol: PzlSymbol)
	
	fun lookup(name: Identifier?): List<PzlSymbol>
}