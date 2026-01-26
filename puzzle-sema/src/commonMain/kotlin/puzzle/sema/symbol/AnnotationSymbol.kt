package puzzle.sema.symbol

import puzzle.ast.declaration.AnnotationDeclaration
import puzzle.ast.expression.Identifier
import puzzle.context.FileContext
import puzzle.sema.scope.AnnotationScope
import puzzle.sema.scope.PzlScope

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