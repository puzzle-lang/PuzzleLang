package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.TypeAliasDeclaration
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.semantics.scope.PzlScope
import puzzle.core.frontend.semantics.scope.TypeAliasScope

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