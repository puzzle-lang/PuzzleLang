package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.TypeAliasDeclaration
import puzzle.core.frontend.semantics.scope.PzlScope

class TypeAliasSymbol(
	override val name: String,
	override val owner: PzlScope,
	override val node: TypeAliasDeclaration,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override val kind = PzlSymbolKind.TYPE_ALIAS
}