package puzzle.sema.deferred

import puzzle.ast.statement.Statement
import puzzle.context.FileContext
import puzzle.sema.binding.declares
import puzzle.sema.scope.PzlScope

class DeferredBodyDeclarer(
	private val scope: PzlScope<FileContext>,
	private val body: List<Statement>,
) : DeferredDeclarer {
	
	context(_: FileContext)
	override fun declares() {
		this.body.declares(scope)
	}
}