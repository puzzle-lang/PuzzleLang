package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.MixinDeclaration
import puzzle.core.frontend.semantics.scope.FileContextScope
import puzzle.core.frontend.semantics.scope.MixinScope

class MixinSymbol(
	override val name: String,
	override val owner: FileContextScope,
	override val node: MixinDeclaration,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override val kind = PzlSymbolKind.MIXIN
	
	override var scope: MixinScope? = null
}