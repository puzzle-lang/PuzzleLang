package puzzle.core.frontend.semantics.checker

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.semantics.symbol.LocalSymbol
import puzzle.core.frontend.semantics.symbol.ParameterSymbol
import puzzle.core.frontend.semantics.symbol.PzlSymbol
import puzzle.core.util.containsType

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
		syntaxError("${symbol.name} 类型声明冲突", symbol.name)
	}
}

context(_: FileContext)
private fun List<PzlSymbol>.checkDuplicateParameterDeclaration(symbol: ParameterSymbol) {
	if (this.containsType<ParameterSymbol>()) {
		syntaxError("${symbol.name} 参数声明冲突", symbol.name)
	}
}

context(_: FileContext)
private fun List<PzlSymbol>.checkDuplicateLocalDeclaration(symbol: LocalSymbol) {
	if (this.containsType<LocalSymbol>()) {
		syntaxError("${symbol.name} 变量声明冲突", symbol.name)
	}
}