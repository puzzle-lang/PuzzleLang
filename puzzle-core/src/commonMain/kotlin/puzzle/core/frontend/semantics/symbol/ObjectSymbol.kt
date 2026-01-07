package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.ObjectDeclaration
import puzzle.core.frontend.semantics.scope.Scope

class ObjectSymbol<S : Scope<S>>(
	override val name: String,
	override val owner: S,
	override val node: ObjectDeclaration,
	override val visibility: Visibility,
) : Symbol<S> {
	
	override val kind = SymbolKind.OBJECT
}