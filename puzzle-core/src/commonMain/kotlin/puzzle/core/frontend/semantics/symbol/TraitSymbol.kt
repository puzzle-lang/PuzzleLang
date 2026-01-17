package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.TraitDeclaration
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.semantics.scope.PzlScope
import puzzle.core.frontend.semantics.scope.TraitScope

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