package puzzle.sema.symbol

import puzzle.ast.expression.Identifier
import puzzle.ast.parameter.Parameter
import puzzle.core.context.FileContext
import puzzle.sema.scope.PzlScope
import puzzle.token.kinds.Visibility

class ParameterSymbol(
	override val name: Identifier,
	override val owner: PzlScope<FileContext>,
	override val node: Parameter,
	override val visibility: Visibility?,
) : PzlSymbol {
	
	override val kind = PzlSymbolKind.PARAMETER
	
	override val isTypeDeclaration = false
}