package puzzle.sema.symbol

import puzzle.ast.declaration.ClassDeclaration
import puzzle.ast.expression.Identifier
import puzzle.context.FileContext
import puzzle.sema.scope.ClassScope
import puzzle.sema.scope.PzlScope

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