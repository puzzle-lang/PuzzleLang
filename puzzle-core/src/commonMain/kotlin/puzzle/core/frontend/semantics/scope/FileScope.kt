package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.semantics.symbol.FileSymbol
import puzzle.core.frontend.semantics.symbol.Symbol

class FileScope(
	override val owner: FileSymbol,
) : Scope<FileScope>() {
	
	var currentParent: Scope<*>? = null
	
	override val parent: Scope<*>?
		get() = currentParent
	
	private val symbolsMap = mutableMapOf<String, MutableList<Symbol<FileScope>>>()
	
	private var cached: List<Symbol<FileScope>>? = null
	
	override val symbols: Collection<Symbol<FileScope>>
		get() = cached ?: symbolsMap.values.flatten().also { cached = it }
	
	override fun declare(symbol: Symbol<FileScope>) {
		val symbols = symbolsMap.getOrPut(symbol.name) { mutableListOf() }
		symbols += symbol
		cached = null
	}
	
	override fun lookup(name: String): List<Symbol<*>> {
		return symbolsMap[name] ?: emptyList()
	}
}