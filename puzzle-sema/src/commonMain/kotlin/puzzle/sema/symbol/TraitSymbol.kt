package puzzle.sema.symbol

import puzzle.ast.declaration.TraitDeclaration
import puzzle.ast.expression.Identifier
import puzzle.core.context.FileContext
import puzzle.sema.scope.PzlScope
import puzzle.sema.scope.TraitScope
import puzzle.token.kinds.Visibility

class TraitSymbol(
	override val name: Identifier,
	override val owner: PzlScope<FileContext>,
	override val node: TraitDeclaration,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override lateinit var scope: TraitScope
	
	override val kind = PzlSymbolKind.TRAIT
	
	override val isTypeDeclaration = true
}