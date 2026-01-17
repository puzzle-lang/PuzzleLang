package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.semantics.scope.ModuleScope
import puzzle.core.frontend.semantics.scope.ProjectScope

class ModuleSymbol(
	override val name: Identifier,
	override val owner: ProjectScope,
) : PzlSymbol {
	
	override lateinit var scope: ModuleScope
	
	override val kind = PzlSymbolKind.MODULE
	
	override val isTypeDeclaration = false
}