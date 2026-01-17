package puzzle.core.frontend.semantics.scope

import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.semantics.symbol.PzlSymbol
import puzzle.core.frontend.semantics.symbol.TypeAliasSymbol

class TypeAliasScope(
	override val parent: PzlScope<FileContext>,
	override val owner: TypeAliasSymbol,
) : PzlScope<FileContext> {
	
	private val symbolsMap = mutableMapOf<Identifier, PzlSymbol>()
	
	override val orderedSymbols = mutableListOf<PzlSymbol>()
	
	context(_: FileContext)
	override fun declare(symbol: PzlSymbol) {
		val name = symbol.name!!
		symbolsMap[name] = symbol
		orderedSymbols += symbol
	}
	
	override fun lookup(name: Identifier?): List<PzlSymbol> {
		return symbolsMap[name]?.let { listOf(it) } ?: emptyList()
	}
}