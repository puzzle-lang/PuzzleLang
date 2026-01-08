package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.semantics.symbol.Symbol

sealed class Scope<S : Scope<S>> {
	
	abstract val parent: Scope<*>?
	
	abstract val owner: Symbol<*>?
	
	abstract fun declare(symbol: Symbol<S>)
	
	abstract fun lookup(name: String): List<Symbol<*>>
	
	abstract val symbols: Collection<Symbol<S>>
	
	fun declares(symbols: List<Symbol<S>>) {
		symbols.forEach { symbol ->
			declare(symbol)
		}
	}
}