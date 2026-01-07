package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.ClassDeclaration
import puzzle.core.frontend.semantics.scope.Scope

class ClassSymbol<S : Scope<S>>(
	override val name: String,
	override val owner: S,
	override val node: ClassDeclaration,
	override val visibility: Visibility,
) : Symbol<S> {
	
	override val kind = SymbolKind.CLASS
}