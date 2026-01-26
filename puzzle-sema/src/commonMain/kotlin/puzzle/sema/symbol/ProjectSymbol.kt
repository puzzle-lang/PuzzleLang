package puzzle.sema.symbol

import puzzle.ast.expression.Identifier
import puzzle.sema.scope.ProjectScope
import puzzle.sema.scope.RootScope

class ProjectSymbol(
	override val name: Identifier,
	override val owner: RootScope,
) : PzlSymbol {
	
	override lateinit var scope: ProjectScope
	
	override val kind = PzlSymbolKind.PROJECT
	
	override val isTypeDeclaration = false
}