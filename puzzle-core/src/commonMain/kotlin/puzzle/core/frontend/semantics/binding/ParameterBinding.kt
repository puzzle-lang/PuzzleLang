package puzzle.core.frontend.semantics.binding

import puzzle.core.frontend.ast.declaration.Property
import puzzle.core.frontend.ast.expression.Argument
import puzzle.core.frontend.ast.parameter.DeclarationContextReceiver
import puzzle.core.frontend.ast.parameter.Parameter
import puzzle.core.frontend.ast.parameter.ParameterReference
import puzzle.core.frontend.ast.parameter.TypeParameter
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.isAnonymousBinding
import puzzle.core.frontend.semantics.deferred.DeferredExpression
import puzzle.core.frontend.semantics.deferred.DeferredTypeReference
import puzzle.core.frontend.semantics.scope.PzlScope
import puzzle.core.frontend.semantics.symbol.*

context(file: FileContext)
fun List<Parameter>.declare(parent: PzlScope<FileContext>) {
	this.forEach {
		val symbol = ParameterSymbol(
			name = it.name,
			owner = parent,
			node = it,
			visibility = it.modifiers.visibility
		)
		parent.declare(symbol)
		if (it.defaultExpression != null) {
			file.deferredExpressions += DeferredExpression(parent, it.defaultExpression)
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
			visibility = it.modifiers.visibility ?: Visibility.PUBLIC
		)
		parent.declare(symbol)
	}
}

context(file: FileContext)
fun List<TypeParameter>.declares(parent: PzlScope<FileContext>) {
	this.forEach {
		val symbol = TypeParameterSymbol(
			name = it.name,
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
fun List<ParameterReference>.declares(parent: PzlScope<FileContext>) {
	this.forEach { it.declare(parent) }
}

context(_: FileContext)
fun ParameterReference.declare(parent: PzlScope<FileContext>) {
	if (this.name.isAnonymousBinding) return
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