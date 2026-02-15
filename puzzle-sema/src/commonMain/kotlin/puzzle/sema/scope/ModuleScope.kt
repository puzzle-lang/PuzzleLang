package puzzle.sema.scope

import puzzle.core.context.ModuleContext
import puzzle.sema.symbol.ModuleSymbol
import puzzle.sema.symbol.PackageSymbol
import puzzle.sema.symbol.PzlSymbol

class ModuleScope(
    override val parent: ProjectScope,
    override val owner: ModuleSymbol?,
) : PzlScope<ModuleContext> {

    private val symbolsByName = mutableMapOf<String?, MutableList<PackageSymbol>>()

    override val orderedSymbols = mutableListOf<PackageSymbol>()

    context(_: ModuleContext)
    override fun declare(symbol: PzlSymbol) {
        symbol as PackageSymbol
        val name = symbol.name!!.value
        val symbols = symbolsByName.getOrPut(name) { mutableListOf() }
        symbols += symbol
        orderedSymbols += symbol
    }

    override fun lookupLocal(name: String?): List<PackageSymbol> {
        return symbolsByName[name].orEmpty()
    }
}