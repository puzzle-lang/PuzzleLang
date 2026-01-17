package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.StructDeclaration
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.semantics.scope.PzlScope
import puzzle.core.frontend.semantics.scope.StructScope

class StructSymbol(
	override val name: Identifier,
	override val owner: PzlScope<FileContext>,
	override val node: StructDeclaration,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override lateinit var scope: StructScope
	
	override val kind = PzlSymbolKind.STRUCT
	
	override val isTypeDeclaration = true
}