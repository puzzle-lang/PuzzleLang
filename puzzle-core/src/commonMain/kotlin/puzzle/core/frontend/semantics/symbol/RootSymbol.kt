package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.semantics.scope.RootScope

class RootSymbol() : PzlSymbol {
	
	override lateinit var scope: RootScope
	
	override val kind = PzlSymbolKind.ROOT
}