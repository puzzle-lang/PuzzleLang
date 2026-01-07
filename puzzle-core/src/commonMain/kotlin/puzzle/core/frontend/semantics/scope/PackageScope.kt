package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.semantics.symbol.PackageSymbol
import puzzle.core.frontend.semantics.symbol.Symbol

class PackageScope(
	override val parent: Scope<*>?,
	override val owner: PackageSymbol<*>,
) : Scope<PackageScope> {
	
	private val symbolsMap = mutableMapOf<String, MutableList<Symbol<PackageScope>>>()
	
	private var cached: List<Symbol<PackageScope>>? = null
	
	override val symbols: Collection<Symbol<PackageScope>>
		get() = cached ?: symbolsMap.values.flatten().also { cached = it }
	
	override fun declare(symbol: Symbol<PackageScope>) {
		val symbols = symbolsMap.getOrPut(symbol.name) { mutableListOf() }
		symbols += symbol
		cached = null
	}
	
	override fun lookupLocal(name: String): Symbol<*>? {
		return symbolsMap[name]?.firstOrNull()
	}
}