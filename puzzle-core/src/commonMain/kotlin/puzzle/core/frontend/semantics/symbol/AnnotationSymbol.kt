package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.AnnotationDeclaration
import puzzle.core.frontend.semantics.scope.Scope

class AnnotationSymbol(
	override val name: String,
	override val owner: Scope,
	override val node: AnnotationDeclaration,
	override val visibility: Visibility,
) : Symbol {
	
	override val kind = SymbolKind.ANNOTATION
}