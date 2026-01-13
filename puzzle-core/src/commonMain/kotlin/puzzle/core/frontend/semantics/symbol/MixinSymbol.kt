package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.MixinDeclaration
import puzzle.core.frontend.semantics.scope.MixinScope
import puzzle.core.frontend.semantics.scope.PzlScope

class MixinSymbol(
	override val name: String,
	override val owner: PzlScope,
	override val node: MixinDeclaration,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override val kind = PzlSymbolKind.MIXIN
	
	override var scope: MixinScope? = null
}