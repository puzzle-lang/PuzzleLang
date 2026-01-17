package puzzle.core.frontend.semantics.deferred

import puzzle.core.frontend.ast.expression.Expression
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.semantics.scope.PzlScope

class DeferredExpression(
	val parent: PzlScope<FileContext>,
	val expression: Expression,
)