package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.model.ModuleContext
import puzzle.core.frontend.semantics.symbol.ModuleSymbol
import puzzle.core.frontend.semantics.symbol.PzlSymbol

class ModuleScope(
	override val parent: ProjectScope,
	override val owner: ModuleSymbol?,
) : PzlScope<ModuleContext> {
	
	private val symbolsByName = mutableMapOf<Identifier, MutableList<PzlSymbol>>()
	
	override val orderedSymbols = mutableListOf<PzlSymbol>()
	
	context(_: ModuleContext)
	override fun declare(symbol: PzlSymbol) {
		val name = symbol.name!!
		val symbols = symbolsByName.getOrPut(name) { mutableListOf() }
		symbols += symbol
		orderedSymbols += symbol
	}
	
	override fun lookup(name: Identifier?): List<PzlSymbol> {
		return symbolsByName[name] ?: emptyList()
	}
}