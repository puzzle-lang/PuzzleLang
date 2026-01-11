package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.EnumDeclaration
import puzzle.core.frontend.semantics.scope.Scope

class EnumSymbol(
	override val name: String,
	override val owner: Scope,
	override val node: EnumDeclaration,
	override val visibility: Visibility,
) : Symbol {
	
	override val kind = SymbolKind.ENUM
}