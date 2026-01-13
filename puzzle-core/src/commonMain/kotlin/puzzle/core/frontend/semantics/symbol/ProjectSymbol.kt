package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.semantics.scope.ProjectScope
import puzzle.core.frontend.semantics.scope.RootScope

class ProjectSymbol(
	override val name: String,
	override val owner: RootScope,
) : PzlSymbol {
	
	override val kind = PzlSymbolKind.PROJECT
	
	override var scope: ProjectScope? = null
}