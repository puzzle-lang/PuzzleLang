package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.semantics.symbol.ObjectSymbol
import puzzle.core.frontend.semantics.symbol.Symbol

class ObjectScope(
	override val parent: Scope<*>?,
	override val owner: ObjectSymbol<*>,
) : Scope<ObjectScope> {
	
	private val symbolsMap = mutableMapOf<String, MutableList<Symbol<ObjectScope>>>()
	
	private var cached: List<Symbol<ObjectScope>>? = null
	
	override val symbols: Collection<Symbol<ObjectScope>>
		get() = cached ?: symbolsMap.values.flatten().also { cached = it }
	
	override fun declare(symbol: Symbol<ObjectScope>) {
		val symbols = symbolsMap.getOrPut(symbol.name) { mutableListOf() }
		symbols += symbol
		cached = null
	}
	
	override fun lookup(name: String): List<Symbol<*>> {
		return symbolsMap[name] ?: parent?.lookup(name) ?: emptyList()
	}
}