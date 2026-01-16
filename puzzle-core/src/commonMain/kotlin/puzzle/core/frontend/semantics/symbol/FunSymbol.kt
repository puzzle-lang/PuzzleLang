package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.FunDeclaration
import puzzle.core.frontend.semantics.scope.FileContextScope

class FunSymbol(
	override val name: String,
	override val owner: FileContextScope,
	override val node: FunDeclaration,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override val kind = PzlSymbolKind.FUN
	
	override var scope: FileContextScope? = null
}