package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.statement.Variable
import puzzle.core.frontend.semantics.scope.Scope

class LocalSymbol<S : Scope<S>>(
	override val name: String,
	override val owner: S,
	override val node: Variable,
) : Symbol<S> {
	
	override val kind = SymbolKind.LOCAL
	
	override val visibility = null
}