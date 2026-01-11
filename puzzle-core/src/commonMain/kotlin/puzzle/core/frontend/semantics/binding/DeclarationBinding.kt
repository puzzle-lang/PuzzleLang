package puzzle.core.frontend.semantics.binding

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.declaration.*
import puzzle.core.frontend.ast.type.SuperConstructorCall
import puzzle.core.frontend.ast.type.SuperType
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.isAnonymousBinding
import puzzle.core.frontend.semantics.scope.*
import puzzle.core.frontend.semantics.symbol.*

context(_: FileContext)
fun List<Declaration>.declares(parent: Scope) {
	this.forEach {
		when (it) {
			is FunDeclaration -> it.declare(parent)
			is CtorDeclaration -> it.declare(parent)
			is InitDeclaration -> it.declare(parent)
			is PropertyDeclaration -> it.declare(parent)
			is ClassDeclaration -> it.declare(parent)
			is ObjectDeclaration -> it.declare(parent)
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

context(_: FileContext)
private fun FunDeclaration.declare(parent: Scope) {
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
	this.typeSpec?.parameters?.declares(scope)
	this.contextSpec?.receivers?.declares(scope)
	this.parameters.declareParameters(scope)
	this.parameters.forEach {
		it.defaultExpression?.declare(scope)
	}
	this.body?.declares(scope)
}

context(_: FileContext)
private fun CtorDeclaration.declare(parent: Scope) {
	val symbol = CtorSymbol(
		name = this.name?.value,
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: Visibility.PUBLIC
	)
	parent.declare(symbol)
	val scope = CtorScope(parent, symbol)
	this.parameters.declareParameters(scope)
	this.parameters.forEach {
		it.defaultExpression?.declare(scope)
	}
	this.body.declares(scope)
}

context(_: FileContext)
private fun InitDeclaration.declare(parent: Scope) {
	val scope = BlockScope(parent)
	this.body.declares(scope)
}

context(_: FileContext)
private fun PropertyDeclaration.declare(parent: Scope) {
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
	this.initializer?.declare(parent)
}

context(_: FileContext)
private fun PropertyGetter.declare(parent: Scope, propertySymbol: PropertySymbol) {
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
	this.oldValue?.declare(scope)
	this.body.declares(scope)
}

context(_: FileContext)
private fun PropertySetter.declare(parent: Scope, propertySymbol: PropertySymbol) {
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
	this.oldValue?.declare(scope)
	this.newValue.declare(scope)
	this.body.declares(scope)
}

context(_: FileContext)
private fun ClassDeclaration.declare(parent: Scope) {
	val symbol = ClassSymbol(
		name = this.name.value,
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: Visibility.PUBLIC
	)
	parent.declare(symbol)
	val scope = ClassScope(parent, symbol)
	this.typeSpec?.parameters?.declares(scope)
	this.contextSpec?.receivers?.declares(scope)
	this.parameters.declareParameters(scope)
	this.parameters.declarePrimaryConstructorProperties(scope)
	this.parameters.forEach {
		it.defaultExpression?.declare(scope)
	}
	this.superTypes.declares(parent)
	this.members.declares(scope)
}

context(_: FileContext)
private fun ObjectDeclaration.declare(parent: Scope) {
	val symbol = ObjectSymbol(
		name = this.name?.value,
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: Visibility.PUBLIC
	)
	parent.declare(symbol)
	val scope = ObjectScope(parent, symbol)
	this.contextSpec?.receivers?.declares(scope)
	this.parameters.declareParameters(scope)
	this.parameters.declarePrimaryConstructorProperties(scope)
	this.parameters.forEach {
		it.defaultExpression?.declare(scope)
	}
	this.superTypes.declares(parent)
	this.members.declares(scope)
}

context(_: FileContext)
private fun TraitDeclaration.declare(parent: Scope) {
	val symbol = TraitSymbol(
		name = this.name.value,
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: Visibility.PUBLIC
	)
	parent.declare(symbol)
	val scope = TraitScope(parent, symbol)
	this.typeSpec?.parameters?.declares(scope)
	this.contextSpec?.receivers?.declares(scope)
	this.members.declares(scope)
}

context(_: FileContext)
private fun MixinDeclaration.declare(parent: Scope) {
	val symbol = MixinSymbol(
		name = this.name.value,
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: Visibility.PUBLIC
	)
	parent.declare(symbol)
	val scope = MixinScope(parent, symbol)
	this.typeSpec?.parameters?.declares(scope)
	this.contextSpec?.receivers?.declares(scope)
	this.members.declares(scope)
}

context(_: FileContext)
private fun StructDeclaration.declare(parent: Scope) {
	val symbol = StructSymbol(
		name = this.name.value,
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: Visibility.PUBLIC
	)
	parent.declare(symbol)
	val scope = StructScope(parent, symbol)
	this.typeSpec?.parameters?.declares(scope)
	this.contextSpec?.receivers?.declares(scope)
	this.parameters.declareParameters(scope)
	this.parameters.declarePrimaryConstructorProperties(scope)
	this.parameters.forEach {
		it.defaultExpression?.declare(scope)
	}
	this.members.declares(scope)
}

context(_: FileContext)
private fun EnumDeclaration.declare(parent: Scope) {
	val symbol = EnumSymbol(
		name = this.name.value,
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: Visibility.PUBLIC
	)
	parent.declare(symbol)
	val scope = EnumScope(parent, symbol)
	this.typeSpec?.parameters?.declares(scope)
	this.contextSpec?.receivers?.declares(scope)
	this.parameters.declareParameters(scope)
	this.parameters.declarePrimaryConstructorProperties(scope)
	this.parameters.forEach {
		it.defaultExpression?.declare(scope)
	}
	this.members.declares(scope)
	this.entries.declares(scope)
}

context(_: FileContext)
private fun EnumEntry.declare(parent: Scope) {
	val symbol = EnumEntrySymbol(
		name = this.name.value,
		owner = parent,
		node = this,
	)
	parent.declare(symbol)
	val scope = EnumEntryScope(parent, symbol)
	this.members.declares(scope)
	this.inits.declares(scope)
}

context(_: FileContext)
private fun ExtensionDeclaration.declare(parent: Scope) {
	val symbol = ExtensionSymbol(
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: Visibility.PUBLIC
	)
	parent.declare(symbol)
	val scope = ExtensionScope(parent, symbol)
	this.typeSpec?.parameters?.declares(scope)
	this.contextSpec?.receivers?.declares(scope)
	this.members.declares(scope)
}

context(_: FileContext)
private fun AnnotationDeclaration.declare(parent: Scope) {
	val symbol = AnnotationSymbol(
		name = this.name.value,
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: Visibility.PUBLIC
	)
	parent.declare(symbol)
}

context(_: FileContext)
private fun TypeAliasDeclaration.declare(parent: Scope) {
	val symbol = TypeAliasSymbol(
		name = this.name.value,
		owner = parent,
		node = this,
		visibility = this.modifiers.visibility ?: Visibility.PUBLIC
	)
	parent.declare(symbol)
}

context(_: FileContext)
private fun List<SuperType>.declares(parent: Scope) {
	this.forEach { type ->
		if (type !is SuperConstructorCall) return@forEach
		type.arguments.declares(parent)
	}
}