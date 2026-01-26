package puzzle.sema.binding

import puzzle.ast.declaration.Property
import puzzle.ast.expression.Argument
import puzzle.ast.parameter.DeclarationContextReceiver
import puzzle.ast.parameter.Parameter
import puzzle.ast.parameter.ParameterReference
import puzzle.ast.parameter.TypeParameter
import puzzle.context.FileContext
import puzzle.sema.deferred.DeferredExpressionDeclarer
import puzzle.sema.deferred.DeferredTypeReferenceDeclarer
import puzzle.sema.deferred.getDeferredDeclarers
import puzzle.sema.scope.PzlScope
import puzzle.sema.symbol.*
import puzzle.sema.util.isAnonymous
import puzzle.token.kinds.ModifierKind.PUBLIC

context(file: FileContext)
fun List<Parameter>.declare(parent: PzlScope<FileContext>) {
	val deferredDeclarers = getDeferredDeclarers()
	this.forEach {
		val symbol = ParameterSymbol(
			name = it.name,
			owner = parent,
			node = it,
			visibility = it.modifiers.visibility
		)
		parent.declare(symbol)
		if (it.defaultExpression != null) {
			deferredDeclarers += DeferredExpressionDeclarer(parent, it.defaultExpression!!)
		}
	}
}

context(file: FileContext)
fun List<Parameter>.declareProperties(parent: PzlScope<FileContext>) {
	this.forEach {
		val isMutable = it.isMutable ?: return@forEach
		val symbol = PropertySymbol(
			name = it.name,
			owner = parent,
			node = Property(
				isMutable = isMutable,
				name = it.name,
				type = it.type,
				location = it.location
			),
			visibility = it.modifiers.visibility ?: PUBLIC
		)
		parent.declare(symbol)
	}
}

context(file: FileContext)
fun List<TypeParameter>.declares(parent: PzlScope<FileContext>) {
	val deferredDeclarers = getDeferredDeclarers()
	this.forEach {
		val symbol = TypeParameterSymbol(
			name = it.name,
			owner = parent,
			node = it,
		)
		parent.declare(symbol)
		if (it.defaultType != null) {
			deferredDeclarers += DeferredTypeReferenceDeclarer(parent, it.defaultType!!)
		}
	}
}

context(_: FileContext)
fun List<ParameterReference>.declares(parent: PzlScope<FileContext>) {
	this.forEach { it.declare(parent) }
}

context(_: FileContext)
fun ParameterReference.declare(parent: PzlScope<FileContext>) {
	if (this.name.isAnonymous) return
	val symbol = LocalSymbol(
		name = this.name,
		owner = parent,
		node = this,
		visibility = null
	)
	parent.declare(symbol)
}

context(_: FileContext)
fun List<DeclarationContextReceiver>.declares(parent: PzlScope<FileContext>) {
	this.forEach {
		val symbol = LocalSymbol(
			name = it.name,
			owner = parent,
			node = it,
			visibility = null
		)
		parent.declare(symbol)
	}
}

context(_: FileContext)
fun List<Argument>.declares(parent: PzlScope<FileContext>) {
	this.forEach {
		it.expression.declare(parent)
	}
}