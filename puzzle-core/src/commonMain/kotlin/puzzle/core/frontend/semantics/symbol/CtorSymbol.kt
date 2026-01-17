package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.CtorDeclaration
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.semantics.scope.CtorScope
import puzzle.core.frontend.semantics.scope.PzlScope

class CtorSymbol(
	override val name: Identifier?,
	override val owner: PzlScope<FileContext>,
	override val node: CtorDeclaration,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override lateinit var scope: CtorScope
	
	override val kind = PzlSymbolKind.CTOR
	
	override val isTypeDeclaration = false
}