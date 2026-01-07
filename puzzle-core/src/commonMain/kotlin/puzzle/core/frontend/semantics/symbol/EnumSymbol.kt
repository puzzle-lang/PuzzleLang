package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.EnumDeclaration
import puzzle.core.frontend.semantics.scope.Scope

class EnumSymbol<S : Scope<S>>(
	override val name: String,
	override val owner: S,
	override val node: EnumDeclaration,
	override val visibility: Visibility,
) : Symbol<S> {
	
	override val kind = SymbolKind.ENUM
}