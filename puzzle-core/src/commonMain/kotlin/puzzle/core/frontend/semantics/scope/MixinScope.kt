package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.semantics.symbol.MixinSymbol
import puzzle.core.frontend.semantics.symbol.Symbol

class MixinScope(
	override val parent: Scope<*>?,
	override val owner: MixinSymbol<*>,
) : Scope<MixinScope> {
	
	private val symbolsMap = mutableMapOf<String, MutableList<Symbol<MixinScope>>>()
	
	private var cached: List<Symbol<MixinScope>>? = null
	
	override val symbols: Collection<Symbol<MixinScope>>
		get() = cached ?: symbolsMap.values.flatten().also { cached = it }
	
	override fun declare(symbol: Symbol<MixinScope>) {
		val symbols = symbolsMap.getOrPut(symbol.name) { mutableListOf() }
		symbols += symbol
		cached = null
	}
	
	override fun lookupLocal(name: String): Symbol<*>? {
		return symbolsMap[name]?.firstOrNull()
			?: parent?.lookupLocal(name)
	}
}