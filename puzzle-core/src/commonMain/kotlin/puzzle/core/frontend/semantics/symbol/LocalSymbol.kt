package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.PzlAstNode
import puzzle.core.frontend.semantics.scope.PzlScope

class LocalSymbol(
	override val name: String,
	override val owner: PzlScope,
	override val node: PzlAstNode,
	override val visibility: Visibility?,
) : PzlSymbol {
	
	override val kind = PzlSymbolKind.LOCAL
}