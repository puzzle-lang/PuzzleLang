package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.FunDeclaration
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.semantics.scope.FunScope
import puzzle.core.frontend.semantics.scope.PzlScope

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