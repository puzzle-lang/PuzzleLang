package puzzle.sema.scope

import puzzle.core.context.Context
import puzzle.core.context.FileContext
import puzzle.sema.symbol.PzlSymbol

sealed interface PzlScope<CTX : Context> {

    val parent: PzlScope<*>

    val owner: PzlSymbol?

    val orderedSymbols: List<PzlSymbol>

    context(_: CTX)
    fun declare(symbol: PzlSymbol)

    fun lookup(name: String?): List<PzlSymbol> = emptyList()

    fun lookupLocal(name: String?): List<PzlSymbol> = emptyList()
}

fun PzlScope<FileContext>.findFileScope(): FileScope {
    var scope: PzlScope<*> = this
    while (scope !is FileScope) {
        scope = scope.parent
    }
    return scope
}