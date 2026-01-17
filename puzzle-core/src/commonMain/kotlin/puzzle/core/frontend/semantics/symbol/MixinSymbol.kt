package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.MixinDeclaration
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.semantics.scope.MixinScope
import puzzle.core.frontend.semantics.scope.PzlScope

class MixinSymbol(
	override val name: Identifier,
	override val owner: PzlScope<FileContext>,
	override val node: MixinDeclaration,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override lateinit var scope: MixinScope
	
	override val kind = PzlSymbolKind.MIXIN
	
	override val isTypeDeclaration = true
}