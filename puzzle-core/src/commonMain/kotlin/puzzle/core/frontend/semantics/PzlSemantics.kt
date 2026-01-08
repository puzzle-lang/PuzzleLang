package puzzle.core.frontend.semantics

import puzzle.core.frontend.ast.AstFile
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.semantics.scope.FileScope
import puzzle.core.frontend.semantics.symbol.FileSymbol

object PzlSemantics {
	
	context(context: FileContext)
	fun analyze(): FileScope {
		return context(context.node) {
			createFileScope()
		}
	}
	
	context(node: AstFile)
	private fun createFileScope(): FileScope {
		val symbol = FileSymbol(node.name, node)
		val scope = FileScope(symbol)
		node.declarations.forEach {
			val symbols = it.toSymbols(scope)
			scope.declares(symbols)
		}
		return scope
	}
}