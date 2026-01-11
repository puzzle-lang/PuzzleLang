package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.TraitDeclaration
import puzzle.core.frontend.semantics.scope.Scope

class TraitSymbol(
	override val name: String,
	override val owner: Scope,
	override val node: TraitDeclaration,
	override val visibility: Visibility,
) : Symbol {
	
	override val kind = SymbolKind.TRAIT
}