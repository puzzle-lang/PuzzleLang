package puzzle.sema.symbol

import puzzle.ast.declaration.CtorDeclaration
import puzzle.ast.expression.Identifier
import puzzle.core.context.FileContext
import puzzle.sema.scope.CtorScope
import puzzle.sema.scope.PzlScope
import puzzle.token.kinds.Visibility

class CtorSymbol(
	override val name: Identifier?,
	override val owner: PzlScope<FileContext>,
	override val node: CtorDeclaration,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override lateinit var scope: CtorScope
	
	override val kind = PzlSymbolKind.CTOR
	
	override val isTypeDeclaration = false
}