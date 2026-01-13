package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.AnnotationDeclaration
import puzzle.core.frontend.semantics.scope.PzlScope

class AnnotationSymbol(
	override val name: String,
	override val owner: PzlScope,
	override val node: AnnotationDeclaration,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override val kind = PzlSymbolKind.ANNOTATION
}