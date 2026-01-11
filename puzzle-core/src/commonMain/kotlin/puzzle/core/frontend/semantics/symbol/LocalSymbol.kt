package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.AstNode
import puzzle.core.frontend.semantics.scope.Scope

class LocalSymbol(
	override val name: String,
	override val owner: Scope,
	override val node: AstNode,
) : Symbol {
	
	override val kind = SymbolKind.LOCAL
	
	override val visibility = null
}