package puzzle.sema.symbol

import puzzle.ast.expression.Identifier
import puzzle.sema.scope.ModuleScope
import puzzle.sema.scope.ProjectScope

class ModuleSymbol(
	override val name: Identifier,
	override val owner: ProjectScope,
) : PzlSymbol {
	
	override lateinit var scope: ModuleScope
	
	override val kind = PzlSymbolKind.MODULE
	
	override val isTypeDeclaration = false
}