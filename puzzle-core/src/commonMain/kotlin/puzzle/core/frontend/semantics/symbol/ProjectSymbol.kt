package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.semantics.scope.ProjectScope
import puzzle.core.frontend.semantics.scope.RootScope

class ProjectSymbol(
	override val name: Identifier,
	override val owner: RootScope,
) : PzlSymbol {
	
	override lateinit var scope: ProjectScope
	
	override val kind = PzlSymbolKind.PROJECT
	
	override val isTypeDeclaration = false
}