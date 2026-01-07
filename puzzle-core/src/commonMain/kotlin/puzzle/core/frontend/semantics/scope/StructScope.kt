package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.semantics.symbol.StructSymbol
import puzzle.core.frontend.semantics.symbol.Symbol

class StructScope(
	override val parent: Scope<*>?,
	override val owner: StructSymbol<*>,
) : Scope<StructScope> {
	
	private val symbolsMap = mutableMapOf<String, MutableList<Symbol<StructScope>>>()
	
	private var cached: List<Symbol<StructScope>>? = null
	
	override val symbols: Collection<Symbol<StructScope>>
		get() = cached ?: symbolsMap.values.flatten().also { cached = it }
	
	override fun declare(symbol: Symbol<StructScope>) {
		val symbols = symbolsMap.getOrPut(symbol.name) { mutableListOf() }
		symbols += symbol
		cached = null
	}
	
	override fun lookupLocal(name: String): Symbol<*>? {
		return symbolsMap[name]?.firstOrNull()
			?: parent?.lookupLocal(name)
	}
}