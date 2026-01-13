package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.TraitDeclaration
import puzzle.core.frontend.semantics.scope.PzlScope
import puzzle.core.frontend.semantics.scope.TraitScope

class TraitSymbol(
	override val name: String,
	override val owner: PzlScope,
	override val node: TraitDeclaration,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override val kind = PzlSymbolKind.TRAIT
	
	override var scope: TraitScope? = null
}