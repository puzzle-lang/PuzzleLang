package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.Declaration
import puzzle.core.frontend.ast.parameter.Parameter
import puzzle.core.frontend.semantics.scope.Scope

class ParameterSymbol<S : Scope<S>>(
	override val name: String,
	override val owner: S,
	override val node: Parameter
) : Symbol<S> {
	
	override val kind = SymbolKind.PARAMETER
	
	override val visibility = null
}