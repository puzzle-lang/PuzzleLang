package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.ErrorDeclaration
import puzzle.core.frontend.semantics.scope.PzlScope

class ErrorSymbol(
	override val name: String,
	override val owner: PzlScope,
	override val node: ErrorDeclaration,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override val kind = PzlSymbolKind.ERROR
}