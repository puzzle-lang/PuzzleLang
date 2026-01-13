package puzzle.core.frontend.semantics.binding

import puzzle.core.frontend.ast.declaration.Property
import puzzle.core.frontend.ast.expression.Argument
import puzzle.core.frontend.ast.parameter.DeclarationContextReceiver
import puzzle.core.frontend.ast.parameter.Parameter
import puzzle.core.frontend.ast.parameter.ParameterReference
import puzzle.core.frontend.ast.parameter.TypeParameter
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.isAnonymousBinding
import puzzle.core.frontend.semantics.scope.PzlScope
import puzzle.core.frontend.semantics.symbol.*

fun List<Parameter>.declareParameters(parent: PzlScope) {
	this.forEach {
		val symbol = LocalSymbol(
			name = it.name.value,
			owner = parent,
			node = it,
		)
		parent.declare(symbol)
	}
}

fun List<Parameter>.declarePrimaryConstructorProperties(parent: PzlScope) {
	this.forEach {
		val isMutable = it.isMutable ?: return@forEach
		val symbol = PropertySymbol(
			name = it.name.value,
			owner = parent,
			node = Property(
				isMutable = isMutable,
				name = it.name,
				type = it.type,
				location = it.location,
			),
			visibility = it.modifiers.visibility ?: Visibility.PUBLIC,
		)
		parent.declare(symbol)
	}
}

fun List<TypeParameter>.declares(parent: PzlScope) {
	this.forEach {
		val symbol = TypeParameterSymbol(
			name = it.name.value,
			owner = parent,
			node = it,
		)
		parent.declare(symbol)
	}
}

fun List<ParameterReference>.declares(parent: PzlScope) {
	this.forEach { it.declare(parent) }
}

fun ParameterReference.declare(parent: PzlScope) {
	if (this.name.isAnonymousBinding) return
	val symbol = LocalSymbol(
		name = this.name.value,
		owner = parent,
		node = this,
	)
	parent.declare(symbol)
}

fun List<DeclarationContextReceiver>.declares(parent: PzlScope) {
	this.forEach {
		val symbol = LocalSymbol(
			name = it.name.value,
			owner = parent,
			node = it
		)
		parent.declare(symbol)
	}
}

context(_: FileContext)
fun List<Argument>.declares(parent: PzlScope) {
	this.forEach {
		it.expression.declare(parent)
	}
}