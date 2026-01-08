package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.semantics.symbol.FunSymbol
import puzzle.core.frontend.semantics.symbol.Symbol

class FunScope(
	override val parent: Scope<*>?,
	override val owner: FunSymbol<*>,
) : Scope<FunScope>() {
	
	private val symbolsMap = mutableMapOf<String, MutableList<Symbol<FunScope>>>()
	
	private var cached: List<Symbol<FunScope>>? = null
	
	override val symbols: Collection<Symbol<FunScope>>
		get() = cached ?: symbolsMap.values.flatten().also { cached = it }
	
	override fun declare(symbol: Symbol<FunScope>) {
		val symbols = symbolsMap.getOrPut(symbol.name) { mutableListOf() }
		symbols += symbol
		cached = null
	}
	
	override fun lookup(name: String): List<Symbol<*>> {
		return symbolsMap[name] ?: parent?.lookup(name) ?: emptyList()
	}
}