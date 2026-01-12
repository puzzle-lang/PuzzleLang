package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.ErrorDeclaration
import puzzle.core.frontend.semantics.scope.Scope

class ErrorSymbol(
	override val name: String,
	override val owner: Scope,
	override val node: ErrorDeclaration,
	override val visibility: Visibility,
) : Symbol {
	
	override val kind = SymbolKind.ERROR
}