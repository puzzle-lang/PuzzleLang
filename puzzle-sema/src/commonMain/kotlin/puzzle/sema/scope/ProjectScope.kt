package puzzle.sema.scope

import puzzle.core.context.ProjectContext
import puzzle.sema.symbol.ModuleSymbol
import puzzle.sema.symbol.ProjectSymbol
import puzzle.sema.symbol.PzlSymbol

class ProjectScope(
    override val parent: RootScope,
    override val owner: ProjectSymbol,
) : PzlScope<ProjectContext> {

    private val symbolsByName = mutableMapOf<String?, MutableList<ModuleSymbol>>()

    override val orderedSymbols = mutableListOf<ModuleSymbol>()

    context(_: ProjectContext)
    override fun declare(symbol: PzlSymbol) {
        symbol as ModuleSymbol
        val name = symbol.name
        val sameNameSymbols = symbolsByName.getOrPut(name.value) { mutableListOf() }
        sameNameSymbols += symbol
        orderedSymbols += symbol
    }

    override fun lookupLocal(name: String?): List<ModuleSymbol> {
        return symbolsByName[name].orEmpty()
    }
}