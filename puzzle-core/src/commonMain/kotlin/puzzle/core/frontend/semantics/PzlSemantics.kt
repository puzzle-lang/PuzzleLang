package puzzle.core.frontend.semantics

import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.semantics.binding.declares
import puzzle.core.frontend.semantics.scope.FileScope
import puzzle.core.frontend.semantics.symbol.FileSymbol

object PzlSemantics {
	
	context(context: FileContext)
	fun analyze(): FileScope {
		val node = context.node
		val symbol = FileSymbol(node.name, node)
		val scope = FileScope(symbol)
		node.declarations.declares(scope)
		return scope
	}
}