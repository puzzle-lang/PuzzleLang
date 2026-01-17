package puzzle.core.frontend.semantics.scope

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.isAnonymousBinding
import puzzle.core.frontend.semantics.checker.checkDuplicate
import puzzle.core.frontend.semantics.symbol.EnumSymbol
import puzzle.core.frontend.semantics.symbol.PzlSymbol
import puzzle.core.frontend.semantics.symbol.PzlSymbolKind

class EnumScope(
	override val parent: PzlScope<FileContext>?,
	override val owner: EnumSymbol,
) : PzlScope<FileContext>, InitContainer {
	
	private val symbolsByName = mutableMapOf<Identifier?, MutableList<PzlSymbol>>()
	
	override val orderedSymbols = mutableListOf<PzlSymbol>()
	
	override val initBlocks = mutableListOf<BlockScope>()
	
	context(_: FileContext)
	override fun declare(symbol: PzlSymbol) {
		val name = symbol.name
		if (name != null && name.isAnonymousBinding) return
		if (name == null && (symbol.kind != PzlSymbolKind.OBJECT && symbol.kind != PzlSymbolKind.CTOR)) {
			syntaxError("${symbol.kind} 不支持默认名称, 默认名称只允许是 object 或 ctor 声明", symbol.node)
		}
		val sameNameSymbols = symbolsByName.getOrPut(name) { mutableListOf() }
		sameNameSymbols.checkDuplicate(symbol)
		sameNameSymbols += symbol
		orderedSymbols += symbol
	}
	
	override fun lookup(name: Identifier?): List<PzlSymbol> {
		return symbolsByName[name] ?: parent?.lookup(name) ?: emptyList()
	}
}