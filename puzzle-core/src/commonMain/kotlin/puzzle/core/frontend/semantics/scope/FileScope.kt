package puzzle.core.frontend.semantics.scope

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.isAnonymousBinding
import puzzle.core.frontend.semantics.checker.checkDuplicate
import puzzle.core.frontend.semantics.symbol.FileSymbol
import puzzle.core.frontend.semantics.symbol.PzlSymbol

class FileScope(
	override val owner: FileSymbol,
) : PzlScope<FileContext> {
	
	override lateinit var parent: PzlScope<*>
	
	private val symbolsByName = mutableMapOf<Identifier, MutableList<PzlSymbol>>()
	
	override val orderedSymbols = mutableListOf<PzlSymbol>()
	
	context(_: FileContext)
	override fun declare(symbol: PzlSymbol) {
		if (symbol.name == null) {
			syntaxError("", symbol.node)
		}
		val name = symbol.name!!
		if (name.isAnonymousBinding) return
		val symbols = symbolsByName.getOrPut(name) { mutableListOf() }
		symbols.checkDuplicate(symbol)
		symbols += symbol
		orderedSymbols += symbol
	}
	
	override fun lookup(name: Identifier?): List<PzlSymbol> {
		return symbolsByName[name] ?: emptyList()
	}
}