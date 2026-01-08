package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.FunDeclaration
import puzzle.core.frontend.semantics.scope.Scope

class FunSymbol<S : Scope<S>>(
	override val name: String,
	override val owner: S,
	override val node: FunDeclaration,
	override val visibility: Visibility,
) : Symbol<S> {
	
	override val kind = SymbolKind.CLASS
}