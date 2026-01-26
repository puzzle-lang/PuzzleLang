package puzzle.sema.symbol

import puzzle.ast.expression.Identifier
import puzzle.sema.scope.PackageScope
import puzzle.sema.scope.PzlScope

class PackageSymbol(
	override val name: Identifier?,
	override val owner: PzlScope<*>,
) : PzlSymbol {
	
	override lateinit var scope: PackageScope
	
	override val kind = PzlSymbolKind.PACKAGE
	
	override val isTypeDeclaration = false
}