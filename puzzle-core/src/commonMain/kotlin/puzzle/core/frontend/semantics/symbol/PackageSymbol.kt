package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.semantics.scope.PackageScope
import puzzle.core.frontend.semantics.scope.PzlScope

class PackageSymbol(
	override val name: Identifier?,
	override val owner: PzlScope<*>,
) : PzlSymbol {
	
	override lateinit var scope: PackageScope
	
	override val kind = PzlSymbolKind.PACKAGE
	
	override val isTypeDeclaration = false
}