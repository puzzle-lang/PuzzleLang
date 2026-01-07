package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.semantics.symbol.ExtensionSymbol
import puzzle.core.frontend.semantics.symbol.Symbol

class ExtensionScope(
	override val parent: Scope<*>?,
	override val owner: ExtensionSymbol<*>,
) : Scope<ExtensionScope> {
	
	private val symbolsMap = mutableMapOf<String, MutableList<Symbol<ExtensionScope>>>()
	
	private var cached: List<Symbol<ExtensionScope>>? = null
	
	override val symbols: Collection<Symbol<ExtensionScope>>
		get() = cached ?: symbolsMap.values.flatten().also { cached = it }
	
	override fun declare(symbol: Symbol<ExtensionScope>) {
		val symbols = symbolsMap.getOrPut(symbol.name) { mutableListOf() }
		symbols += symbol
		cached = null
	}
	
	override fun lookupLocal(name: String): Symbol<*>? {
		return symbolsMap[name]?.firstOrNull()
			?: parent?.lookupLocal(name)
	}
}