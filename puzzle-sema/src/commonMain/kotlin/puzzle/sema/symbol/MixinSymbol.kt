package puzzle.sema.symbol

import puzzle.ast.declaration.MixinDeclaration
import puzzle.ast.expression.Identifier
import puzzle.core.context.FileContext
import puzzle.sema.scope.MixinScope
import puzzle.sema.scope.PzlScope
import puzzle.token.kinds.Visibility

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