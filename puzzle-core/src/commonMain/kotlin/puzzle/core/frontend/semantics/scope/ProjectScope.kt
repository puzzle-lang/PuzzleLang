package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.model.ProjectContext
import puzzle.core.frontend.semantics.symbol.ProjectSymbol
import puzzle.core.frontend.semantics.symbol.PzlSymbol

class ProjectScope(
	override val parent: RootScope,
	override val owner: ProjectSymbol,
) : PzlScope<ProjectContext> {
	
	private val symbolsMap = mutableMapOf<Identifier, PzlSymbol>()
	
	override val orderedSymbols = mutableListOf<PzlSymbol>()
	
	context(_: ProjectContext)
	override fun declare(symbol: PzlSymbol) {
		val name = symbol.name!!
		symbolsMap[name] = symbol
		orderedSymbols += symbol
	}
	
	override fun lookup(name: Identifier?): List<PzlSymbol> {
		return symbolsMap[name]?.let { listOf(it) } ?: parent.lookup(name)
	}
}