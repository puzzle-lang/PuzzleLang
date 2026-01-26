package puzzle.sema.symbol

import puzzle.ast.declaration.TypeAliasDeclaration
import puzzle.ast.expression.Identifier
import puzzle.context.FileContext
import puzzle.sema.scope.PzlScope
import puzzle.sema.scope.TypeAliasScope

class TypeAliasSymbol(
	override val name: Identifier,
	override val owner: PzlScope<FileContext>,
	override val node: TypeAliasDeclaration,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override lateinit var scope: TypeAliasScope
	
	override val kind = PzlSymbolKind.TYPEALIAS
	
	override val isTypeDeclaration = true
}