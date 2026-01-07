package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.semantics.symbol.EnumSymbol
import puzzle.core.frontend.semantics.symbol.Symbol

class EnumScope(
	override val parent: Scope<*>?,
	override val owner: EnumSymbol<*>,
) : Scope<EnumScope> {
	
	private val symbolsMap = mutableMapOf<String, MutableList<Symbol<EnumScope>>>()
	
	private var cached: List<Symbol<EnumScope>>? = null
	
	override val symbols: Collection<Symbol<EnumScope>>
		get() = cached ?: symbolsMap.values.flatten().also { cached = it }
	
	override fun declare(symbol: Symbol<EnumScope>) {
		val symbols = symbolsMap.getOrPut(symbol.name) { mutableListOf() }
		symbols += symbol
		cached = null
	}
	
	override fun lookupLocal(name: String): Symbol<*>? {
		return symbolsMap[name]?.firstOrNull()
			?: parent?.lookupLocal(name)
	}
}