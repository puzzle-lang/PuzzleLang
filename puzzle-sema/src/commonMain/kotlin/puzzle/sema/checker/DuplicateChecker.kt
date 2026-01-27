package puzzle.sema.checker

import puzzle.core.util.containsType
import puzzle.core.context.FileContext
import puzzle.sema.semaError
import puzzle.sema.symbol.LocalSymbol
import puzzle.sema.symbol.ParameterSymbol
import puzzle.sema.symbol.PzlSymbol

context(_: FileContext)
fun List<PzlSymbol>.checkDuplicate(symbol: PzlSymbol) {
	if (this.isEmpty()) return
	when {
		symbol.isTypeDeclaration -> this.checkDuplicateTypeDeclaration(symbol)
		symbol is ParameterSymbol -> this.checkDuplicateParameterDeclaration(symbol)
		symbol is LocalSymbol -> this.checkDuplicateLocalDeclaration(symbol)
	}
}

context(_: FileContext)
private fun List<PzlSymbol>.checkDuplicateTypeDeclaration(symbol: PzlSymbol) {
	if (this.any { it.isTypeDeclaration }) {
		semaError("${symbol.name} 类型声明冲突", symbol.name)
	}
}

context(_: FileContext)
private fun List<PzlSymbol>.checkDuplicateParameterDeclaration(symbol: ParameterSymbol) {
	if (this.containsType<ParameterSymbol>()) {
		semaError("${symbol.name} 参数声明冲突", symbol.name)
	}
}

context(_: FileContext)
private fun List<PzlSymbol>.checkDuplicateLocalDeclaration(symbol: LocalSymbol) {
	if (this.containsType<LocalSymbol>()) {
		semaError("${symbol.name} 变量声明冲突", symbol.name)
	}
}