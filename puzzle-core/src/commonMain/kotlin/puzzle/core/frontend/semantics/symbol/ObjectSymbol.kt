package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.ObjectDeclaration
import puzzle.core.frontend.semantics.scope.ObjectScope
import puzzle.core.frontend.semantics.scope.PzlScope

class ObjectSymbol(
	override val name: String?,
	override val owner: PzlScope,
	override val node: ObjectDeclaration,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override val kind = PzlSymbolKind.OBJECT
	
	override var scope: ObjectScope? = null
}