package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.TypeAliasDeclaration
import puzzle.core.frontend.semantics.scope.Scope

class TypeAliasSymbol(
	override val name: String,
	override val owner: Scope,
	override val node: TypeAliasDeclaration,
	override val visibility: Visibility,
) : Symbol {
	
	override val kind = SymbolKind.TYPE_ALIAS
}