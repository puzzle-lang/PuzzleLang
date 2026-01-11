package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.semantics.symbol.Symbol

sealed interface Scope {
	
	val parent: Scope?
	
	val owner: Symbol?
	
	fun declare(symbol: Symbol)
	
	fun lookup(name: String): List<Symbol>
	
	val symbols: Collection<Symbol>
}