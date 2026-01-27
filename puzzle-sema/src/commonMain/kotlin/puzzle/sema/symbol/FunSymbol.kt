package puzzle.sema.symbol

import puzzle.ast.declaration.FunDeclaration
import puzzle.ast.expression.Identifier
import puzzle.core.context.FileContext
import puzzle.sema.scope.FunScope
import puzzle.sema.scope.PzlScope
import puzzle.token.kinds.Visibility

class FunSymbol(
	override val name: Identifier,
	override val owner: PzlScope<FileContext>,
	override val node: FunDeclaration,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override lateinit var scope: FunScope
	
	override val kind = PzlSymbolKind.FUN
	
	override val isTypeDeclaration = false
}