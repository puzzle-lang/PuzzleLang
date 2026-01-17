package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.ObjectDeclaration
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.semantics.scope.ObjectScope
import puzzle.core.frontend.semantics.scope.PzlScope

class ObjectSymbol(
	override val name: Identifier?,
	override val owner: PzlScope<FileContext>,
	override val node: ObjectDeclaration,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override lateinit var scope: ObjectScope
	
	override val kind = PzlSymbolKind.OBJECT
	
	override val isTypeDeclaration = true
}