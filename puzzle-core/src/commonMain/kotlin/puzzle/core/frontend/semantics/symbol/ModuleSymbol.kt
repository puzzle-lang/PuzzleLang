package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.semantics.scope.ModuleScope
import puzzle.core.frontend.semantics.scope.ProjectScope

class ModuleSymbol(
	override val name: String,
	override val owner: ProjectScope,
) : PzlSymbol {
	
	override val kind = PzlSymbolKind.MODULE
	
	override var scope: ModuleScope? = null
}