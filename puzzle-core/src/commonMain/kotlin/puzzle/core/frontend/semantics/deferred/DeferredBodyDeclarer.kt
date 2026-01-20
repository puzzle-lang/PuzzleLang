package puzzle.core.frontend.semantics.deferred

import puzzle.core.frontend.ast.statement.Statement
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.semantics.binding.declares
import puzzle.core.frontend.semantics.scope.PzlScope

class DeferredBodyDeclarer(
	private val scope: PzlScope<FileContext>,
	private val body: List<Statement>,
) : DeferredDeclarer {
	
	context(_: FileContext)
	override fun declares() {
		this.body.declares(scope)
	}
}