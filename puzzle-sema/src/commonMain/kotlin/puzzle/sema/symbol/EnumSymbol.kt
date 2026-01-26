package puzzle.sema.symbol

import puzzle.ast.declaration.EnumDeclaration
import puzzle.ast.expression.Identifier
import puzzle.context.FileContext
import puzzle.sema.scope.EnumScope
import puzzle.sema.scope.PzlScope

class EnumSymbol(
	override val name: Identifier,
	override val owner: PzlScope<FileContext>,
	override val node: EnumDeclaration,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override lateinit var scope: EnumScope
	
	override val kind = PzlSymbolKind.ENUM
	
	override val isTypeDeclaration = true
}