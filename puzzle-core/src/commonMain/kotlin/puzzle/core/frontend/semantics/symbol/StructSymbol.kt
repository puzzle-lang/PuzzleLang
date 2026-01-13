package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.StructDeclaration
import puzzle.core.frontend.semantics.scope.PzlScope
import puzzle.core.frontend.semantics.scope.StructScope

class StructSymbol(
	override val name: String,
	override val owner: PzlScope,
	override val node: StructDeclaration,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override val kind = PzlSymbolKind.STRUCT
	
	override var scope: StructScope? = null
}