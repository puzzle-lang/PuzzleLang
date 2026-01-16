package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.model.*
import puzzle.core.frontend.semantics.symbol.PzlSymbol

sealed interface PzlScope<C : Context> {
	
	val parent: PzlScope<*>?
	
	val owner: PzlSymbol?
	
	context(_: C)
	fun declare(symbol: PzlSymbol)
	
	fun lookup(name: String): List<PzlSymbol>
	
	val symbols: Collection<PzlSymbol>
}

typealias FileContextScope = PzlScope<FileContext>

typealias ModuleContextScope = PzlScope<ModuleContext>

typealias ProjectContextScope = PzlScope<ProjectContext>

typealias RootContextScope = PzlScope<RootContext>

sealed interface InitContainer {
	
	val initBlocks: MutableList<BlockScope>
}