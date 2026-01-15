package puzzle.core.frontend.semantics.binding

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.declaration.*
import puzzle.core.frontend.ast.type.SuperConstructorCall
import puzzle.core.frontend.ast.type.SuperType
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.isAnonymousBinding
import puzzle.core.frontend.semantics.deferred.*
import puzzle.core.frontend.semantics.scope.*
import puzzle.core.frontend.semantics.symbol.*

context(_: FileContext)
fun List<Declaration>.declares(parent: PzlScope) {
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
			is EnumEntry -> it.declare(parent)
			is ExtensionDeclaration -> it.declare(parent)
			is AnnotationDeclaration -> it.declare(parent)
			is TypeAliasDeclaration -> it.declare(parent)
		}
	}
}

context(context: FileContext)
private fun FunDeclaration.declare(parent: PzlScope) {
	val name = when (this.name) {
		is IdentifierFunName -> this.name.name.value
		is MagicFunName -> this.name.kind.value
		is SymbolFunName -> this.name.symbol.kind.value
	}
	val symbol = FunSymbol(
		name = name,
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: Visibility.PUBLIC
	)
	parent.declare(symbol)
	val scope = FunScope(parent, symbol)
	symbol.scope = scope
	this.typeSpec?.parameters?.declares(scope)
	this.contextSpec?.receivers?.declares(scope)
	this.parameters.declare(scope)
	context.deferredScopes += DeferredFunScope(scope, this)
}

context(context: FileContext)
private fun CtorDeclaration.declare(parent: PzlScope) {
	val symbol = CtorSymbol(
		name = this.name?.value,
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: Visibility.PUBLIC
	)
	parent.declare(symbol)
	val scope = CtorScope(parent, symbol)
	symbol.scope = scope
	this.parameters.declare(scope)
	context.deferredScopes += DeferredCtorScope(scope, this)
}

context(context: FileContext)
private fun InitDeclaration.declare(parent: PzlScope) {
	val scope = BlockScope(parent)
	parent as InitContainer
	parent.initBlocks += scope
	context.deferredScopes += DeferredInitScope(scope, this)
}

context(context: FileContext)
private fun PropertyDeclaration.declare(parent: PzlScope) {
	val visibility = this.modifiers.visibility ?: Visibility.PUBLIC
	val properties = when (this.propertySpec) {
		is DestructurePropertySpec -> this.propertySpec.properties
		is SinglePropertySpec -> listOf(this.propertySpec.property)
	}
	val symbols = properties.mapNotNull {
		if (it.name.isAnonymousBinding) return@mapNotNull null
		val symbol = PropertySymbol(
			name = it.name.value,
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
		context.deferredExpressions += DeferredPropertyExpression(parent, this.initializer)
	}
}

context(context: FileContext)
private fun PropertyGetter.declare(parent: PzlScope, propertySymbol: PropertySymbol) {
	val visibility = this.modifiers.visibility?.also {
		if (it != propertySymbol.visibility) {
			syntaxError("属性访问器的可见性必须和属性的可见性相同", this.modifiers.first())
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
	context.deferredScopes += DeferredGetterScope(scope, this)
}

context(context: FileContext)
private fun PropertySetter.declare(parent: PzlScope, propertySymbol: PropertySymbol) {
	val visibility = this.modifiers.visibility?.also {
		if (it > propertySymbol.visibility) {
			syntaxError("属性赋值器的可见性不能大于属性的可见性", this.modifiers.first())
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
	context.deferredScopes += DeferredSetterScope(scope, this)
}

context(_: FileContext)
private fun ClassDeclaration.declare(parent: PzlScope) {
	val symbol = ClassSymbol(
		name = this.name.value,
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: Visibility.PUBLIC
	)
	parent.declare(symbol)
	val scope = ClassScope(parent, symbol)
	symbol.scope = scope
	this.typeSpec?.parameters?.declares(scope)
	this.contextSpec?.receivers?.declares(scope)
	this.parameters.declare(scope)
	this.superTypes.declares(parent)
	this.members.declares(scope)
}

context(_: FileContext)
private fun ObjectDeclaration.declare(parent: PzlScope) {
	val symbol = ObjectSymbol(
		name = this.name?.value,
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: Visibility.PUBLIC
	)
	parent.declare(symbol)
	val scope = ObjectScope(parent, symbol)
	symbol.scope = scope
	this.contextSpec?.receivers?.declares(scope)
	this.parameters.declare(scope)
	this.superTypes.declares(parent)
	this.members.declares(scope)
}

context(_: FileContext)
private fun ErrorDeclaration.declare(parent: PzlScope) {
	val symbol = ErrorSymbol(
		name = this.name.value,
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: Visibility.PUBLIC
	)
	parent.declare(symbol)
}

context(_: FileContext)
private fun TraitDeclaration.declare(parent: PzlScope) {
	val symbol = TraitSymbol(
		name = this.name.value,
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: Visibility.PUBLIC
	)
	parent.declare(symbol)
	val scope = TraitScope(parent, symbol)
	symbol.scope = scope
	this.typeSpec?.parameters?.declares(scope)
	this.contextSpec?.receivers?.declares(scope)
	this.members.declares(scope)
}

context(_: FileContext)
private fun MixinDeclaration.declare(parent: PzlScope) {
	val symbol = MixinSymbol(
		name = this.name.value,
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: Visibility.PUBLIC
	)
	parent.declare(symbol)
	val scope = MixinScope(parent, symbol)
	symbol.scope = scope
	this.typeSpec?.parameters?.declares(scope)
	this.contextSpec?.receivers?.declares(scope)
	this.members.declares(scope)
}

context(_: FileContext)
private fun StructDeclaration.declare(parent: PzlScope) {
	val symbol = StructSymbol(
		name = this.name.value,
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: Visibility.PUBLIC
	)
	parent.declare(symbol)
	val scope = StructScope(parent, symbol)
	symbol.scope = scope
	this.typeSpec?.parameters?.declares(scope)
	this.contextSpec?.receivers?.declares(scope)
	this.parameters.declare(scope)
	this.members.declares(scope)
}

context(_: FileContext)
private fun EnumDeclaration.declare(parent: PzlScope) {
	val symbol = EnumSymbol(
		name = this.name.value,
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: Visibility.PUBLIC
	)
	parent.declare(symbol)
	val scope = EnumScope(parent, symbol)
	symbol.scope = scope
	this.typeSpec?.parameters?.declares(scope)
	this.contextSpec?.receivers?.declares(scope)
	this.parameters.declare(scope)
	this.members.declares(scope)
	this.entries.declares(scope)
}

context(_: FileContext)
private fun EnumEntry.declare(parent: PzlScope) {
	val symbol = EnumEntrySymbol(
		name = this.name.value,
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
private fun ExtensionDeclaration.declare(parent: PzlScope) {
	val symbol = ExtensionSymbol(
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: Visibility.PUBLIC
	)
	parent.declare(symbol)
	val scope = ExtensionScope(parent, symbol)
	symbol.scope = scope
	this.typeSpec?.parameters?.declares(scope)
	this.contextSpec?.receivers?.declares(scope)
	this.members.declares(scope)
}

context(_: FileContext)
private fun AnnotationDeclaration.declare(parent: PzlScope) {
	val symbol = AnnotationSymbol(
		name = this.name.value,
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: Visibility.PUBLIC
	)
	parent.declare(symbol)
}

context(_: FileContext)
private fun TypeAliasDeclaration.declare(parent: PzlScope) {
	val symbol = TypeAliasSymbol(
		name = this.name.value,
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: Visibility.PUBLIC
	)
	parent.declare(symbol)
}

context(_: FileContext)
private fun List<SuperType>.declares(parent: PzlScope) {
	this.forEach { type ->
		if (type !is SuperConstructorCall) return@forEach
		type.arguments.declares(parent)
	}
}