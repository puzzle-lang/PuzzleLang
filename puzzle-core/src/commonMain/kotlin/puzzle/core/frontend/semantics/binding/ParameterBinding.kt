package puzzle.core.frontend.semantics.binding

import puzzle.core.frontend.ast.expression.Argument
import puzzle.core.frontend.ast.parameter.DeclarationContextReceiver
import puzzle.core.frontend.ast.parameter.Parameter
import puzzle.core.frontend.ast.parameter.ParameterReference
import puzzle.core.frontend.ast.parameter.TypeParameter
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.isAnonymousBinding
import puzzle.core.frontend.semantics.deferred.DeferredParameterExpression
import puzzle.core.frontend.semantics.deferred.DeferredTypeReference
import puzzle.core.frontend.semantics.scope.FileContextScope
import puzzle.core.frontend.semantics.symbol.LocalSymbol
import puzzle.core.frontend.semantics.symbol.TypeParameterSymbol
import puzzle.core.frontend.semantics.symbol.visibility

context(file: FileContext)
fun List<Parameter>.declare(parent: FileContextScope) {
	this.forEach {
		val symbol = LocalSymbol(
			name = it.name.value,
			owner = parent,
			node = it,
			visibility = it.modifiers.visibility
		)
		parent.declare(symbol)
		if (it.defaultExpression != null) {
			file.deferredExpressions += DeferredParameterExpression(parent, it.defaultExpression)
		}
	}
}

context(file: FileContext)
fun List<TypeParameter>.declares(parent: FileContextScope) {
	this.forEach {
		val symbol = TypeParameterSymbol(
			name = it.name.value,
			owner = parent,
			node = it,
		)
		parent.declare(symbol)
		if (it.defaultType != null) {
			file.deferredTypeReferences += DeferredTypeReference(parent, it.defaultType)
		}
	}
}

context(_: FileContext)
fun List<ParameterReference>.declares(parent: FileContextScope) {
	this.forEach { it.declare(parent) }
}

context(_: FileContext)
fun ParameterReference.declare(parent: FileContextScope) {
	if (this.name.isAnonymousBinding) return
	val symbol = LocalSymbol(
		name = this.name.value,
		owner = parent,
		node = this,
		visibility = null
	)
	parent.declare(symbol)
}

context(_: FileContext)
fun List<DeclarationContextReceiver>.declares(parent: FileContextScope) {
	this.forEach {
		val symbol = LocalSymbol(
			name = it.name.value,
			owner = parent,
			node = it,
			visibility = null
		)
		parent.declare(symbol)
	}
}

context(_: FileContext)
fun List<Argument>.declares(parent: FileContextScope) {
	this.forEach {
		it.expression.declare(parent)
	}
}