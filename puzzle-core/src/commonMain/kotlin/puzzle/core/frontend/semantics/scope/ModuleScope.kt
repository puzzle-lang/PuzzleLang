package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.semantics.symbol.ModuleSymbol
import puzzle.core.frontend.semantics.symbol.Symbol

class ModuleScope(
	override val parent: ProjectScope,
	override val owner: ModuleSymbol?,
) : Scope<ModuleScope> {
	
	private val symbolsMap = mutableMapOf<String, MutableList<Symbol<ModuleScope>>>()
	
	private var cached: List<Symbol<ModuleScope>>? = null
	
	override val symbols: Collection<Symbol<ModuleScope>>
		get() = cached ?: symbolsMap.values.flatten().also { cached = it }
	
	override fun declare(symbol: Symbol<ModuleScope>) {
		val symbols = symbolsMap.getOrPut(symbol.name) { mutableListOf() }
		symbols += symbol
		cached = null
	}
	
	override fun lookup(name: String): List<Symbol<*>> {
		return symbolsMap[name] ?: emptyList()
	}
}