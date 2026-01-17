package puzzle.core.frontend.semantics.binding

import puzzle.core.frontend.ast.statement.*
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.isAnonymousBinding
import puzzle.core.frontend.semantics.scope.BlockScope
import puzzle.core.frontend.semantics.scope.PzlScope
import puzzle.core.frontend.semantics.symbol.LocalSymbol

context(_: FileContext)
fun List<Statement>.declares(parent: PzlScope<FileContext>) {
	this.forEach {
		when (it) {
			is AssignmentStatement -> it.declare(parent)
			is ContextualStatement -> it.declare(parent)
			is ForStatement -> it.declare(parent)
			is IfStatement -> it.declare(parent)
			is InitStatement -> it.declare(parent)
			is VariableDeclarationStatement -> it.declare(parent)
			is WhileStatement -> it.declare(parent)
			is ExpressionStatement -> it.declare(parent)
		}
	}
}

context(_: FileContext)
private fun AssignmentStatement.declare(parent: PzlScope<FileContext>) {
	this.target.declare(parent)
	this.value.declare(parent)
}

context(_: FileContext)
private fun ContextualStatement.declare(parent: PzlScope<FileContext>) {
	this.arguments.declares(parent)
}

context(_: FileContext)
private fun ForStatement.declare(parent: PzlScope<FileContext>) {
	this.iterable.declare(parent)
	val scope = BlockScope(parent)
	val patterns = when (this.pattern) {
		is ForValuePattern -> listOf(this.pattern.reference)
		is ForDestructurePattern -> this.pattern.references
	}
	patterns.forEach {
		if (it.name.isAnonymousBinding) return@forEach
		val symbol = LocalSymbol(
			name = it.name,
			owner = scope,
			node = pattern,
			visibility = null
		)
		scope.declare(symbol)
	}
	this.body.declares(parent)
}

context(_: FileContext)
private fun IfStatement.declare(parent: PzlScope<FileContext>) {
	this.condition.declare(parent)
	val scope = BlockScope(parent)
	this.thenBody.declares(scope)
}

context(_: FileContext)
private fun InitStatement.declare(parent: PzlScope<FileContext>) {
	this.arguments.declares(parent)
}

context(_: FileContext)
private fun ExpressionStatement.declare(parent: PzlScope<FileContext>) {
	this.expression.declare(parent)
}

context(_: FileContext)
private fun WhileStatement.declare(parent: PzlScope<FileContext>) {
	this.condition.declare(parent)
	val scope = BlockScope(parent)
	this.body.declares(scope)
}

context(_: FileContext)
private fun VariableDeclarationStatement.declare(parent: PzlScope<FileContext>) {
	val variables = when (this.variableSpec) {
		is DestructureVariableSpec -> this.variableSpec.variables
		is SingleVariableSpec -> listOf(this.variableSpec.variable)
	}
	variables.forEach {
		if (it.name.isAnonymousBinding) return@forEach
		val symbol = LocalSymbol(
			name = it.name,
			owner = parent,
			node = it,
			visibility = null
		)
		parent.declare(symbol)
	}
	this.initializer?.declare(parent)
}