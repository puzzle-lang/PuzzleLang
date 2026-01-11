package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.ExtensionDeclaration
import puzzle.core.frontend.semantics.scope.Scope

class ExtensionSymbol(
	override val owner: Scope,
	override val node: ExtensionDeclaration,
	override val visibility: Visibility,
) : Symbol {
	
	override val kind = SymbolKind.EXTENSION
	
	override val name = null
	
	val extendedType get() = node.extendedType
}