package puzzle.sema.symbol

import puzzle.ast.declaration.ErrorDeclaration
import puzzle.ast.expression.Identifier
import puzzle.core.context.FileContext
import puzzle.sema.scope.ErrorScope
import puzzle.sema.scope.PzlScope
import puzzle.token.kinds.Visibility

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