package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.ClassDeclaration
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.semantics.scope.ClassScope
import puzzle.core.frontend.semantics.scope.PzlScope

class ClassSymbol(
	override val name: Identifier,
	override val owner: PzlScope<FileContext>,
	override val node: ClassDeclaration,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override lateinit var scope: ClassScope
	
	override val kind = PzlSymbolKind.CLASS
	
	override val isTypeDeclaration = true
}