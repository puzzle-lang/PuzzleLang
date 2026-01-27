package puzzle.sema.symbol

import puzzle.ast.declaration.ObjectDeclaration
import puzzle.ast.expression.Identifier
import puzzle.core.context.FileContext
import puzzle.sema.scope.ObjectScope
import puzzle.sema.scope.PzlScope
import puzzle.token.kinds.Visibility

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