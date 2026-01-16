package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.EnumDeclaration
import puzzle.core.frontend.semantics.scope.EnumScope
import puzzle.core.frontend.semantics.scope.FileContextScope

class EnumSymbol(
	override val name: String,
	override val owner: FileContextScope,
	override val node: EnumDeclaration,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override val kind = PzlSymbolKind.ENUM
	
	override var scope: EnumScope? = null
}