package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.AnnotationDeclaration
import puzzle.core.frontend.semantics.scope.Scope

class AnnotationSymbol<S : Scope<S>>(
	override val name: String,
	override val owner: S,
	override val node: AnnotationDeclaration,
	override val visibility: Visibility,
) : Symbol<S> {
	
	override val kind = SymbolKind.ANNOTATION
}