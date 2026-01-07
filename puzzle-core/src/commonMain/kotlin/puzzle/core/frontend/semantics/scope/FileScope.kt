package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.semantics.symbol.FileSymbol
import puzzle.core.frontend.semantics.symbol.Symbol

class FileScope(
	override val parent: Scope<*>?,
	override val owner: FileSymbol<*>,
) : Scope<FileScope> {
	
	private val symbolsMap = mutableMapOf<String, MutableList<Symbol<FileScope>>>()
	
	private var cached: List<Symbol<FileScope>>? = null
	
	override val symbols: Collection<Symbol<FileScope>>
		get() = cached ?: symbolsMap.values.flatten().also { cached = it }
	
	override fun declare(symbol: Symbol<FileScope>) {
		val symbols = symbolsMap.getOrPut(symbol.name) { mutableListOf() }
		symbols += symbol
		cached = null
	}
	
	override fun lookupLocal(name: String): Symbol<*>? {
		return symbolsMap[name]?.firstOrNull()
			?: parent?.lookupLocal(name)
	}
}