package puzzle.sema.symbol

import puzzle.ast.expression.Identifier
import puzzle.ast.parameter.TypeParameter
import puzzle.context.FileContext
import puzzle.sema.scope.PzlScope

class TypeParameterSymbol(
	override val name: Identifier,
	override val owner: PzlScope<FileContext>,
	override val node: TypeParameter,
) : PzlSymbol {
	
	override val kind = PzlSymbolKind.TYPE_PARAMETER
	
	override val isTypeDeclaration = true
}