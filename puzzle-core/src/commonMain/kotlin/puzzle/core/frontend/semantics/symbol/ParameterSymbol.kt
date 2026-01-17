package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.ast.parameter.Parameter
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.semantics.scope.PzlScope

class ParameterSymbol(
	override val name: Identifier,
	override val owner: PzlScope<FileContext>,
	override val node: Parameter,
	override val visibility: Visibility?,
) : PzlSymbol {
	
	override val kind = PzlSymbolKind.PARAMETER
	
	override val isTypeDeclaration = false
}