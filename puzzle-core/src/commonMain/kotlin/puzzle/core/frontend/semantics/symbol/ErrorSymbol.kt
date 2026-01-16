package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.ErrorDeclaration
import puzzle.core.frontend.semantics.scope.FileContextScope

class ErrorSymbol(
	override val name: String,
	override val owner: FileContextScope,
	override val node: ErrorDeclaration,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override val kind = PzlSymbolKind.ERROR
}