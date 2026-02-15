package puzzle.sema.scope

import puzzle.core.context.RootContext
import puzzle.sema.symbol.ProjectSymbol
import puzzle.sema.symbol.PzlSymbol

object RootScope : PzlScope<RootContext> {

    override val owner: PzlSymbol
        get() = error("RootScope 没有 owner")

    override val parent: PzlScope<*>
        get() = error("RootScope 没有 parent")

    private val symbolsByName = mutableMapOf<String?, MutableList<ProjectSymbol>>()

    override val orderedSymbols = mutableListOf<ProjectSymbol>()

    context(_: RootContext)
    override fun declare(symbol: PzlSymbol) {
        symbol as ProjectSymbol
        val name = symbol.name
        val sameNameSymbols = symbolsByName.getOrPut(name.value) { mutableListOf() }
        sameNameSymbols += symbol
        orderedSymbols += symbol
    }

    override fun lookupLocal(name: String?): List<PzlSymbol> {
        return symbolsByName[name].orEmpty()
    }
}