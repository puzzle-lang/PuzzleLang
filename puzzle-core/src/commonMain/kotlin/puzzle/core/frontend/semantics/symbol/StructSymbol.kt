package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.StructDeclaration
import puzzle.core.frontend.semantics.scope.Scope

class StructSymbol(
	override val name: String,
	override val owner: Scope,
	override val node: StructDeclaration,
	override val visibility: Visibility,
) : Symbol {
	
	override val kind = SymbolKind.STRUCT
}