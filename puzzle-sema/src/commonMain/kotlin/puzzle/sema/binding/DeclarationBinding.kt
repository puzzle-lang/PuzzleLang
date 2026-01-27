package puzzle.sema.binding

import puzzle.ast.declaration.*
import puzzle.ast.type.SuperConstructorCall
import puzzle.ast.type.SuperType
import puzzle.core.context.FileContext
import puzzle.sema.deferred.DeferredBodyDeclarer
import puzzle.sema.deferred.DeferredExpressionDeclarer
import puzzle.sema.deferred.getDeferredDeclarers
import puzzle.sema.scope.*
import puzzle.sema.semaError
import puzzle.sema.symbol.*
import puzzle.sema.util.isAnonymous
import puzzle.token.kinds.ModifierKind.PUBLIC

context(_: FileContext)
fun List<Declaration>.declares(parent: PzlScope<FileContext>) {
	this.forEach {
		when (it) {
			is FunDeclaration -> it.declare(parent)
			is CtorDeclaration -> it.declare(parent)
			is InitDeclaration -> it.declare(parent)
			is PropertyDeclaration -> it.declare(parent)
			is ClassDeclaration -> it.declare(parent)
			is ObjectDeclaration -> it.declare(parent)
			is ErrorDeclaration -> it.declare(parent)
			is TraitDeclaration -> it.declare(parent)
			is MixinDeclaration -> it.declare(parent)
			is StructDeclaration -> it.declare(parent)
			is EnumDeclaration -> it.declare(parent)
			is EnumEntry -> it.declare(parent as EnumScope)
			is ExtensionDeclaration -> it.declare(parent)
			is AnnotationDeclaration -> it.declare(parent)
			is TypeAliasDeclaration -> it.declare(parent)
		}
	}
}

context(file: FileContext)
private fun FunDeclaration.declare(parent: PzlScope<FileContext>) {
	val symbol = FunSymbol(
		name = this.name.name,
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: PUBLIC
	)
	parent.declare(symbol)
	val scope = FunScope(parent, symbol)
	symbol.scope = scope
	this.typeSpec?.parameters?.declares(scope)
	this.contextSpec?.receivers?.declares(scope)
	this.parameters.declare(scope)
	if (this.body != null) {
		val deferredDeclarers = getDeferredDeclarers()
		deferredDeclarers += DeferredBodyDeclarer(scope, this.body!!)
	}
}

context(file: FileContext)
private fun CtorDeclaration.declare(parent: PzlScope<FileContext>) {
	val symbol = CtorSymbol(
		name = this.name,
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: PUBLIC
	)
	parent.declare(symbol)
	val scope = CtorScope(parent, symbol)
	symbol.scope = scope
	this.parameters.declare(scope)
	val deferredDeclarers = getDeferredDeclarers()
	deferredDeclarers += DeferredBodyDeclarer(scope, this.body)
}

context(file: FileContext)
private fun InitDeclaration.declare(parent: PzlScope<FileContext>) {
	val scope = BlockScope(parent)
	parent as InitContainer
	parent.initBlocks += scope
	val deferredDeclarers = getDeferredDeclarers()
	deferredDeclarers += DeferredBodyDeclarer(scope, this.body)
}

context(file: FileContext)
private fun PropertyDeclaration.declare(parent: PzlScope<FileContext>) {
	val visibility = this.modifiers.visibility ?: PUBLIC
	val properties = when (val propertySpec = this.propertySpec) {
		is DestructurePropertySpec -> propertySpec.properties
		is SinglePropertySpec -> listOf(propertySpec.property)
	}
	val symbols = properties.mapNotNull {
		if (it.name.isAnonymous) return@mapNotNull null
		val symbol = PropertySymbol(
			name = it.name,
			owner = parent,
			node = it,
			visibility = visibility
		)
		parent.declare(symbol)
		symbol
	}
	val propertySymbol = symbols.first()
	this.getter?.declare(parent, propertySymbol)
	this.setter?.declare(parent, propertySymbol)
	if (this.initializer != null) {
		val deferredDeclarers = getDeferredDeclarers()
		deferredDeclarers += DeferredExpressionDeclarer(parent, this.initializer!!)
	}
}

context(file: FileContext)
private fun PropertyGetter.declare(parent: PzlScope<FileContext>, propertySymbol: PropertySymbol) {
	val visibility = this.modifiers.visibility?.also {
		if (it != propertySymbol.visibility) {
			semaError("属性访问器的可见性必须和属性的可见性相同", this.modifiers.first())
		}
	} ?: propertySymbol.visibility
	val symbol = PropertyGetterSymbol(
		name = propertySymbol.name,
		owner = parent,
		node = this,
		visibility = visibility
	)
	parent.declare(symbol)
	val scope = BlockScope(parent, symbol)
	symbol.scope = scope
	this.oldValue?.declare(scope)
	val deferredDeclarers = getDeferredDeclarers()
	deferredDeclarers += DeferredBodyDeclarer(parent, this.body)
}

context(file: FileContext)
private fun PropertySetter.declare(parent: PzlScope<FileContext>, propertySymbol: PropertySymbol) {
	val visibility = this.modifiers.visibility?.also {
		if (it > propertySymbol.visibility) {
			semaError("属性赋值器的可见性不能大于属性的可见性", this.modifiers.first())
		}
	} ?: propertySymbol.visibility
	val symbol = PropertySetterSymbol(
		name = propertySymbol.name,
		owner = parent,
		node = this,
		visibility = visibility
	)
	parent.declare(symbol)
	val scope = BlockScope(parent, symbol)
	symbol.scope = scope
	this.oldValue?.declare(scope)
	this.newValue.declare(scope)
	val deferredDeclarers = getDeferredDeclarers()
	deferredDeclarers += DeferredBodyDeclarer(parent, this.body)
}

context(_: FileContext)
private fun ClassDeclaration.declare(parent: PzlScope<FileContext>) {
	val symbol = ClassSymbol(
		name = this.name,
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: PUBLIC
	)
	parent.declare(symbol)
	val scope = ClassScope(parent, symbol)
	symbol.scope = scope
	this.typeSpec?.parameters?.declares(scope)
	this.contextSpec?.receivers?.declares(scope)
	this.parameters.declare(scope)
	this.parameters.declareProperties(scope)
	this.superTypes.declares(parent)
	this.members.declares(scope)
}

context(_: FileContext)
private fun ObjectDeclaration.declare(parent: PzlScope<FileContext>) {
	val symbol = ObjectSymbol(
		name = this.name,
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: PUBLIC
	)
	parent.declare(symbol)
	val scope = ObjectScope(parent, symbol)
	symbol.scope = scope
	this.contextSpec?.receivers?.declares(scope)
	this.parameters.declare(scope)
	this.parameters.declareProperties(scope)
	this.superTypes.declares(parent)
	this.members.declares(scope)
}

context(_: FileContext)
private fun ErrorDeclaration.declare(parent: PzlScope<FileContext>) {
	val symbol = ErrorSymbol(
		name = this.name,
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: PUBLIC
	)
	parent.declare(symbol)
	val scope = ErrorScope(parent, symbol)
	symbol.scope = scope
	this.parameters.declare(scope)
	this.parameters.declareProperties(scope)
}

context(_: FileContext)
private fun TraitDeclaration.declare(parent: PzlScope<FileContext>) {
	val symbol = TraitSymbol(
		name = this.name,
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: PUBLIC
	)
	parent.declare(symbol)
	val scope = TraitScope(parent, symbol)
	symbol.scope = scope
	this.typeSpec?.parameters?.declares(scope)
	this.contextSpec?.receivers?.declares(scope)
	this.members.declares(scope)
}

context(_: FileContext)
private fun MixinDeclaration.declare(parent: PzlScope<FileContext>) {
	val symbol = MixinSymbol(
		name = this.name,
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: PUBLIC
	)
	parent.declare(symbol)
	val scope = MixinScope(parent, symbol)
	symbol.scope = scope
	this.typeSpec?.parameters?.declares(scope)
	this.contextSpec?.receivers?.declares(scope)
	this.members.declares(scope)
}

context(_: FileContext)
private fun StructDeclaration.declare(parent: PzlScope<FileContext>) {
	val symbol = StructSymbol(
		name = this.name,
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: PUBLIC
	)
	parent.declare(symbol)
	val scope = StructScope(parent, symbol)
	symbol.scope = scope
	this.typeSpec?.parameters?.declares(scope)
	this.contextSpec?.receivers?.declares(scope)
	this.parameters.declare(scope)
	this.parameters.declareProperties(scope)
	this.members.declares(scope)
}

context(_: FileContext)
private fun EnumDeclaration.declare(parent: PzlScope<FileContext>) {
	val symbol = EnumSymbol(
		name = this.name,
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: PUBLIC
	)
	parent.declare(symbol)
	val scope = EnumScope(parent, symbol)
	symbol.scope = scope
	this.typeSpec?.parameters?.declares(scope)
	this.contextSpec?.receivers?.declares(scope)
	this.parameters.declare(scope)
	this.parameters.declareProperties(scope)
	this.members.declares(scope)
	this.entries.declares(scope)
}

context(_: FileContext)
private fun EnumEntry.declare(parent: EnumScope) {
	val symbol = EnumEntrySymbol(
		name = this.name,
		owner = parent,
		node = this,
	)
	parent.declare(symbol)
	val scope = EnumEntryScope(parent, symbol)
	symbol.scope = scope
	this.members.declares(scope)
	this.inits.declares(scope)
}

context(_: FileContext)
private fun ExtensionDeclaration.declare(parent: PzlScope<FileContext>) {
	val symbol = ExtensionSymbol(
		name = this.alias,
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: PUBLIC
	)
	parent.declare(symbol)
	val scope = ExtensionScope(parent, symbol)
	symbol.scope = scope
	this.typeSpec?.parameters?.declares(scope)
	this.contextSpec?.receivers?.declares(scope)
	this.members.declares(scope)
}

context(_: FileContext)
private fun AnnotationDeclaration.declare(parent: PzlScope<FileContext>) {
	val symbol = AnnotationSymbol(
		name = this.name,
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: PUBLIC
	)
	parent.declare(symbol)
	val scope = AnnotationScope(parent, symbol)
	symbol.scope = scope
	this.parameters.declare(scope)
	this.parameters.declareProperties(scope)
}

context(_: FileContext)
private fun TypeAliasDeclaration.declare(parent: PzlScope<FileContext>) {
	val symbol = TypeAliasSymbol(
		name = this.name,
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: PUBLIC
	)
	parent.declare(symbol)
	val scope = TypeAliasScope(parent, symbol)
	symbol.scope = scope
	this.typeSpec?.parameters?.declares(scope)
}

context(file: FileContext)
private fun List<SuperType>.declares(parent: PzlScope<FileContext>) {
	val deferredDeclarers = getDeferredDeclarers()
	this.forEach { superType ->
		if (superType !is SuperConstructorCall) return@forEach
		superType.arguments.forEach {
			deferredDeclarers += DeferredExpressionDeclarer(parent, it.expression)
		}
	}
}