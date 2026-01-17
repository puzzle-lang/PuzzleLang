package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.ExtensionDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.semantics.scope.ExtensionScope
import puzzle.core.frontend.semantics.scope.PzlScope

class ExtensionSymbol(
	override val owner: PzlScope<FileContext>,
	override val node: ExtensionDeclaration,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override lateinit var scope: ExtensionScope
	
	override val kind = PzlSymbolKind.EXTENSION
	
	override val isTypeDeclaration = false
}