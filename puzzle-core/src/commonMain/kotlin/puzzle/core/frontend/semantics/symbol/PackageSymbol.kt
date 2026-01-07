package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.AstNode
import puzzle.core.frontend.ast.declaration.Declaration
import puzzle.core.frontend.semantics.scope.Scope

class PackageSymbol<S : Scope<S>>(
	override val name: String,
	override val owner: S,
) : Symbol<S> {
	
	override val kind = SymbolKind.PACKAGE
	
	override val node = null
	
	override val visibility = null
}