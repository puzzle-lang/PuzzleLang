package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.TypeAliasDeclaration
import puzzle.core.frontend.semantics.scope.Scope

class TypeAliasSymbol<S : Scope<S>>(
	override val name: String,
	override val owner: S,
	override val node: TypeAliasDeclaration,
	override val visibility: Visibility,
) : Symbol<S> {
	
	override val kind = SymbolKind.TYPE_ALIAS
}