package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.FunDeclaration
import puzzle.core.frontend.semantics.scope.Scope

class FunSymbol(
	override val name: String,
	override val owner: Scope,
	override val node: FunDeclaration,
	override val visibility: Visibility,
) : Symbol {
	
	override val kind = SymbolKind.FUN
}