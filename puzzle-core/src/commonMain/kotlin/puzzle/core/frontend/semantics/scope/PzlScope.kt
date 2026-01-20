package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.model.Context
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.semantics.symbol.PzlSymbol

sealed interface PzlScope<CTX : Context> {
	
	val parent: PzlScope<*>
	
	val owner: PzlSymbol?
	
	val symbolsByName: Map<String?, MutableList<PzlSymbol>>
	
	val orderedSymbols: List<PzlSymbol>
	
	context(_: CTX)
	fun declare(symbol: PzlSymbol)
	
	fun lookup(name: String?): List<PzlSymbol> = emptyList()
	
	fun lookupLocal(name: String?): List<PzlSymbol> = emptyList()
}

fun PzlScope<FileContext>.findFileScope(): FileScope {
	var scope: PzlScope<*> = this
	while (scope !is FileScope) {
		scope = scope.parent
	}
	return scope
}

fun PzlScope<FileContext>.findRootScope(): RootScope {
	var scope: PzlScope<*> = this
	while (scope !is RootScope) {
		scope = scope.parent
	}
	return scope
}