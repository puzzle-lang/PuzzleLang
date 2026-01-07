package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.semantics.symbol.Symbol

sealed interface Scope<S : Scope<S>> {
	
	val parent: Scope<*>?
	
	val owner: Symbol<*>?
	
	fun declare(symbol: Symbol<S>)
	
	fun lookupLocal(name: String): Symbol<*>?
	
	val symbols: Collection<Symbol<S>>
}