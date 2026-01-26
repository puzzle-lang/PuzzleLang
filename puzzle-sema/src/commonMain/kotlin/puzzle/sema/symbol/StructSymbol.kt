package puzzle.sema.symbol

import puzzle.ast.declaration.StructDeclaration
import puzzle.ast.expression.Identifier
import puzzle.context.FileContext
import puzzle.sema.scope.PzlScope
import puzzle.sema.scope.StructScope

class StructSymbol(
	override val name: Identifier,
	override val owner: PzlScope<FileContext>,
	override val node: StructDeclaration,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override lateinit var scope: StructScope
	
	override val kind = PzlSymbolKind.STRUCT
	
	override val isTypeDeclaration = true
}