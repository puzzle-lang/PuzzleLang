package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.TraitDeclaration
import puzzle.core.frontend.semantics.scope.Scope

class TraitSymbol<S : Scope<S>>(
	override val name: String,
	override val owner: S,
	override val node: TraitDeclaration,
	override val visibility: Visibility,
) : Symbol<S> {
	
	override val kind = SymbolKind.TRAIT
}