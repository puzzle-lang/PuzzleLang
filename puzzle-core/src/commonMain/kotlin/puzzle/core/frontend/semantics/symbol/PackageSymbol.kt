package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.semantics.scope.PackageScope
import puzzle.core.frontend.semantics.scope.PzlScope

class PackageSymbol(
	override val name: String?,
	override val owner: PzlScope,
) : PzlSymbol {
	
	override val kind = PzlSymbolKind.PACKAGE
	
	override var scope: PackageScope? = null
}