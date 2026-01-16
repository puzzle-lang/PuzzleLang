package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.ObjectDeclaration
import puzzle.core.frontend.semantics.scope.FileContextScope
import puzzle.core.frontend.semantics.scope.ObjectScope

class ObjectSymbol(
	override val name: String?,
	override val owner: FileContextScope,
	override val node: ObjectDeclaration,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override val kind = PzlSymbolKind.OBJECT
	
	override var scope: ObjectScope? = null
}