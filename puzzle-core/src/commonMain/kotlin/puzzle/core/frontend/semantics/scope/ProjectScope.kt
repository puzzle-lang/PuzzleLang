package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.semantics.symbol.ProjectSymbol
import puzzle.core.frontend.semantics.symbol.Symbol

class ProjectScope(
	override val parent: RootScope,
	override val owner: ProjectSymbol,
) : Scope<ProjectScope> {
	
	private val symbolsMap = mutableMapOf<String, MutableList<Symbol<ProjectScope>>>()
	
	private var cached: List<Symbol<ProjectScope>>? = null
	
	override val symbols: Collection<Symbol<ProjectScope>>
		get() = cached ?: symbolsMap.values.flatten().also { cached = it }
	
	override fun declare(symbol: Symbol<ProjectScope>) {
		val symbols = symbolsMap.getOrPut(symbol.name) { mutableListOf() }
		symbols += symbol
		cached = null
	}
	
	override fun lookupLocal(name: String): Symbol<*>? {
		return symbolsMap[name]?.firstOrNull()
	}
}