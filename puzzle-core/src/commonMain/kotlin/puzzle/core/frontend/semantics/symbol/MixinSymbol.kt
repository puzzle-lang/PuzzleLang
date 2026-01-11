package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.MixinDeclaration
import puzzle.core.frontend.semantics.scope.Scope

class MixinSymbol(
	override val name: String,
	override val owner: Scope,
	override val node: MixinDeclaration,
	override val visibility: Visibility,
) : Symbol {
	
	override val kind = SymbolKind.MIXIN
}