package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.ExtensionDeclaration
import puzzle.core.frontend.semantics.scope.Scope

class ExtensionSymbol<S : Scope<S>>(
	override val owner: S,
	override val node: ExtensionDeclaration,
	override val visibility: Visibility,
) : Symbol<S> {
	
	override val kind = SymbolKind.EXTENSION
	
	override val name = ""
	
	val extendedType get() = node.extendedType
}