package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.CtorDeclaration
import puzzle.core.frontend.semantics.scope.CtorScope
import puzzle.core.frontend.semantics.scope.FileContextScope

class CtorSymbol(
	override val name: String?,
	override val owner: FileContextScope,
	override val node: CtorDeclaration,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override val kind = PzlSymbolKind.CTOR
	
	override var scope: CtorScope? = null
}