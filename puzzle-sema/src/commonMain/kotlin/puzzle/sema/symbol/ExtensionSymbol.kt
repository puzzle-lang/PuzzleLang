package puzzle.sema.symbol

import puzzle.ast.declaration.ExtensionDeclaration
import puzzle.ast.expression.Identifier
import puzzle.context.FileContext
import puzzle.sema.scope.ExtensionScope
import puzzle.sema.scope.PzlScope

class ExtensionSymbol(
	override val name: Identifier,
	override val owner: PzlScope<FileContext>,
	override val node: ExtensionDeclaration,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override lateinit var scope: ExtensionScope
	
	override val kind = PzlSymbolKind.EXTENSION
	
	override val isTypeDeclaration = false
}