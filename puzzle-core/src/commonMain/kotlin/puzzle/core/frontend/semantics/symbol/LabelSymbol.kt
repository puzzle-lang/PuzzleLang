package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.semantics.scope.BlockScope

class LabelSymbol(
	override val name: String,
	override val owner: BlockScope,
	override val node: Identifier,
) : Symbol<BlockScope> {
	
	override val kind = SymbolKind.LABEL
	
	override val visibility = null
}