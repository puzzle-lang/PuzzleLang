package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.isAnonymousBinding
import puzzle.core.frontend.semantics.checker.checkDuplicate
import puzzle.core.frontend.semantics.symbol.PzlSymbol

class BlockScope(
	override val parent: PzlScope<FileContext>,
	override val owner: PzlSymbol? = null,
) : PzlScope<FileContext> {
	
	private val symbolsByName = mutableMapOf<String, MutableList<PzlSymbol>>()
	
	override val orderedSymbols = mutableListOf<PzlSymbol>()
	
	context(_: FileContext)
	override fun declare(symbol: PzlSymbol) {
		val name = symbol.name!!
		if (name.isAnonymousBinding) return
		val sameNameSymbols = symbolsByName.getOrPut(name.value) { mutableListOf() }
		sameNameSymbols.checkDuplicate(symbol)
		sameNameSymbols += symbol
		orderedSymbols += symbol
	}
	
	override fun lookup(name: String?): List<PzlSymbol> {
		return symbolsByName[name] ?: parent.lookup(name)
	}
}

class A {
	
	class B {
		
		class C
	}
	
	val a: B.C = B.C()
}