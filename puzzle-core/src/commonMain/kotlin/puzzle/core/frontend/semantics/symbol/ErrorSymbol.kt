package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.ErrorDeclaration
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.semantics.scope.ErrorScope
import puzzle.core.frontend.semantics.scope.PzlScope

class ErrorSymbol(
	override val name: Identifier,
	override val owner: PzlScope<FileContext>,
	override val node: ErrorDeclaration,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override val kind = PzlSymbolKind.ERROR
	
	override lateinit var scope: ErrorScope
	
	override val isTypeDeclaration = true
}