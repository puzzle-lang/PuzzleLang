package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.parameter.TypeParameter
import puzzle.core.frontend.semantics.scope.Scope

class TypeParameterSymbol(
	override val name: String,
	override val owner: Scope,
	override val node: TypeParameter,
) : Symbol {
	
	override val kind = SymbolKind.TYPE_PARAMETER
	
	override val visibility = null
}