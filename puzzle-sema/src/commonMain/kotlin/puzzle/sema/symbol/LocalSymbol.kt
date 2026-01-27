package puzzle.sema.symbol

import puzzle.ast.PzlAstNode
import puzzle.ast.expression.Identifier
import puzzle.core.context.FileContext
import puzzle.sema.scope.PzlScope
import puzzle.token.kinds.Visibility

class LocalSymbol(
	override val name: Identifier,
	override val owner: PzlScope<FileContext>,
	override val node: PzlAstNode,
	override val visibility: Visibility?,
) : PzlSymbol {
	
	override val kind = PzlSymbolKind.LOCAL
	
	override val isTypeDeclaration = false
}