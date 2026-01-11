package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.CtorDeclaration
import puzzle.core.frontend.semantics.scope.Scope

class CtorSymbol(
	override val name: String?,
	override val owner: Scope,
	override val node: CtorDeclaration,
	override val visibility: Visibility,
) : Symbol {
	
	override val kind = SymbolKind.CTOR
}