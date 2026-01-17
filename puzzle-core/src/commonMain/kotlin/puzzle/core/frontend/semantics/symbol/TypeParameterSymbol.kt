package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.ast.parameter.TypeParameter
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.semantics.scope.PzlScope

class TypeParameterSymbol(
	override val name: Identifier,
	override val owner: PzlScope<FileContext>,
	override val node: TypeParameter,
) : PzlSymbol {
	
	override val kind = PzlSymbolKind.TYPE_PARAMETER
	
	override val isTypeDeclaration = true
}