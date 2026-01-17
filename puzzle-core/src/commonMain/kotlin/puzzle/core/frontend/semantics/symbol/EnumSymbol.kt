package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.EnumDeclaration
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.semantics.scope.EnumScope
import puzzle.core.frontend.semantics.scope.PzlScope

class EnumSymbol(
	override val name: Identifier,
	override val owner: PzlScope<FileContext>,
	override val node: EnumDeclaration,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override lateinit var scope: EnumScope
	
	override val kind = PzlSymbolKind.ENUM
	
	override val isTypeDeclaration = true
}