package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.semantics.symbol.ClassSymbol
import puzzle.core.frontend.semantics.symbol.Symbol

class ClassScope(
	override val parent: Scope<*>?,
	override val owner: ClassSymbol<*>,
) : Scope<ClassScope> {
	
	private val symbolsMap = mutableMapOf<String, MutableList<Symbol<ClassScope>>>()
	
	private var cached: List<Symbol<ClassScope>>? = null
	
	override val symbols: Collection<Symbol<ClassScope>>
		get() = cached ?: symbolsMap.values.flatten().also { cached = it }
	
	override fun declare(symbol: Symbol<ClassScope>) {
		val symbols = symbolsMap.getOrPut(symbol.name) { mutableListOf() }
		symbols += symbol
		cached = null
	}
	
	override fun lookupLocal(name: String): Symbol<*>? {
		return symbolsMap[name]?.firstOrNull()
			?: parent?.lookupLocal(name)
	}
}