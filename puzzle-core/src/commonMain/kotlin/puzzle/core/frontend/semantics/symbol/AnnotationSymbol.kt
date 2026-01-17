package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.AnnotationDeclaration
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.semantics.scope.AnnotationScope
import puzzle.core.frontend.semantics.scope.PzlScope

class AnnotationSymbol(
	override val name: Identifier,
	override val owner: PzlScope<FileContext>,
	override val node: AnnotationDeclaration,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override lateinit var scope: AnnotationScope
	
	override val kind = PzlSymbolKind.ANNOTATION
	
	override val isTypeDeclaration = true
}