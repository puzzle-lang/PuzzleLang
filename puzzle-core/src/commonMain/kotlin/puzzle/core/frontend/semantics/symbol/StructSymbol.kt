package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.StructDeclaration
import puzzle.core.frontend.semantics.scope.Scope

class StructSymbol<S : Scope<S>>(
	override val name: String,
	override val owner: S,
	override val node: StructDeclaration,
	override val visibility: Visibility,
) : Symbol<S> {
	
	override val kind = SymbolKind.STRUCT
}