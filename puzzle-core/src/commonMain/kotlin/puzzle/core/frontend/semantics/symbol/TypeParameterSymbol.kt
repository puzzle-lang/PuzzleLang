package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.parameter.TypeParameter
import puzzle.core.frontend.semantics.scope.PzlScope

class TypeParameterSymbol(
	override val name: String,
	override val owner: PzlScope,
	override val node: TypeParameter,
) : PzlSymbol {
	
	override val kind = PzlSymbolKind.TYPE_PARAMETER
}