package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.ClassDeclaration
import puzzle.core.frontend.semantics.scope.ClassScope
import puzzle.core.frontend.semantics.scope.FileContextScope

class ClassSymbol(
	override val name: String,
	override val owner: FileContextScope,
	override val node: ClassDeclaration,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override val kind = PzlSymbolKind.CLASS
	
	override var scope: ClassScope? = null
}