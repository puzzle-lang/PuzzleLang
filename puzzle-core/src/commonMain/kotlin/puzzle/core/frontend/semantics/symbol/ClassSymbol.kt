package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.ClassDeclaration
import puzzle.core.frontend.semantics.scope.Scope

class ClassSymbol(
	override val name: String,
	override val owner: Scope,
	override val node: ClassDeclaration,
	override val visibility: Visibility,
) : Symbol {
	
	override val kind = SymbolKind.CLASS
}