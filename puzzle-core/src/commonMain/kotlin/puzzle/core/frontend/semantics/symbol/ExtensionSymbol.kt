package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.ExtensionDeclaration
import puzzle.core.frontend.semantics.scope.ExtensionScope
import puzzle.core.frontend.semantics.scope.FileContextScope

class ExtensionSymbol(
	override val owner: FileContextScope,
	override val node: ExtensionDeclaration,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override val kind = PzlSymbolKind.EXTENSION
	
	override val name = null
	
	override var scope: ExtensionScope? = null
}