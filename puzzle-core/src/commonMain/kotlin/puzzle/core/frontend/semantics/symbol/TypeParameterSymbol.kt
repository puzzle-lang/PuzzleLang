package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.parameter.TypeParameter
import puzzle.core.frontend.semantics.scope.Scope

class TypeParameterSymbol<S : Scope<S>>(
	override val name: String,
	override val owner: S,
	override val node: TypeParameter,
) : Symbol<S> {
	
	override val kind = SymbolKind.TYPE_PARAMETER
	
	override val visibility = null
}