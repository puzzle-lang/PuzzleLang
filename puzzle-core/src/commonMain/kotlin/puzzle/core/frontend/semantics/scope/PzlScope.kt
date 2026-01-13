package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.semantics.symbol.PzlSymbol

sealed interface PzlScope {
	
	val parent: PzlScope?
	
	val owner: PzlSymbol?
	
	fun declare(symbol: PzlSymbol)
	
	fun lookup(name: String): List<PzlSymbol>
	
	val symbols: Collection<PzlSymbol>
}

sealed interface InitContainer {
	
	val initBlocks: MutableList<BlockScope>
}