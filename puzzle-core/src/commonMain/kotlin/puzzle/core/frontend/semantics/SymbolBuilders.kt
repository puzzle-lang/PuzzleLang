package puzzle.core.frontend.semantics

import puzzle.core.frontend.ast.declaration.*
import puzzle.core.frontend.semantics.scope.Scope
import puzzle.core.frontend.semantics.symbol.*

fun <S : Scope<S>> TopLevelAllowedDeclaration.toSymbols(owner: S): List<Symbol<S>> {
	return when (this) {
		is AnnotationDeclaration -> listOf(this.toSymbol(owner))
		is ClassDeclaration -> listOf(this.toSymbol(owner))
		is EnumDeclaration -> listOf(this.toSymbol(owner))
		is ExtensionDeclaration -> listOf(this.toSymbol(owner))
		is FunDeclaration -> listOf(this.toSymbol(owner))
		is MixinDeclaration -> listOf(this.toSymbol(owner))
		is ObjectDeclaration -> listOf(this.toSymbol(owner))
		is PropertyDeclaration -> this.toSymbols(owner)
		is StructDeclaration -> listOf(this.toSymbol(owner))
		is TraitDeclaration -> listOf(this.toSymbol(owner))
		is TypeAliasDeclaration -> listOf(this.toSymbol(owner))
	}
}

private fun <S : Scope<S>> AnnotationDeclaration.toSymbol(owner: S): AnnotationSymbol<S> {
	return AnnotationSymbol(
		name = this.name.value,
		owner = owner,
		node = this,
		visibility = this.modifiers.getVisibility()
	)
}

private fun <S : Scope<S>> ClassDeclaration.toSymbol(owner: S): ClassSymbol<S> {
	return ClassSymbol(
		name = this.name.value,
		owner = owner,
		node = this,
		visibility = this.modifiers.getVisibility()
	)
}

private fun <S : Scope<S>> EnumDeclaration.toSymbol(owner: S): EnumSymbol<S> {
	return EnumSymbol(
		name = this.name.value,
		owner = owner,
		node = this,
		visibility = this.modifiers.getVisibility()
	)
}

private fun <S : Scope<S>> ExtensionDeclaration.toSymbol(owner: S): ExtensionSymbol<S> {
	return ExtensionSymbol(
		owner = owner,
		node = this,
		visibility = this.modifiers.getVisibility()
	)
}

private fun <S : Scope<S>> FunDeclaration.toSymbol(owner: S): FunSymbol<S> {
	val name = when (this.name) {
		is IdentifierFunName -> this.name.name.value
		is MagicFunName -> this.name.kind.value
		is SymbolFunName -> this.name.symbol.kind.value
	}
	return FunSymbol(
		name = name,
		owner = owner,
		node = this,
		visibility = this.modifiers.getVisibility()
	)
}

private fun <S : Scope<S>> MixinDeclaration.toSymbol(owner: S): MixinSymbol<S> {
	return MixinSymbol(
		name = this.name.value,
		owner = owner,
		node = this,
		visibility = this.modifiers.getVisibility()
	)
}

private fun <S : Scope<S>> ObjectDeclaration.toSymbol(owner: S): ObjectSymbol<S> {
	return ObjectSymbol(
		name = this.name?.value ?: "",
		owner = owner,
		node = this,
		visibility = this.modifiers.getVisibility()
	)
}

private fun <S : Scope<S>> PropertyDeclaration.toSymbols(owner: S): List<PropertySymbol<S>> {
	return when (this.propertySpec) {
		is DestructurePropertySpec -> {
			this.propertySpec.properties.map { property ->
				PropertySymbol(
					name = property.name.value,
					owner = owner,
					node = property,
					visibility = this.modifiers.getVisibility(),
				)
			}
		}
		
		is SinglePropertySpec -> {
			val property = this.propertySpec.property
			val symbol = PropertySymbol(
				name = property.name.value,
				owner = owner,
				node = property,
				visibility = this.modifiers.getVisibility(),
			)
			listOf(symbol)
		}
	}
}

private fun <S : Scope<S>> StructDeclaration.toSymbol(owner: S): StructSymbol<S> {
	return StructSymbol(
		name = this.name.value,
		owner = owner,
		node = this,
		visibility = this.modifiers.getVisibility()
	)
}

private fun <S : Scope<S>> TraitDeclaration.toSymbol(owner: S): TraitSymbol<S> {
	return TraitSymbol(
		name = this.name.value,
		owner = owner,
		node = this,
		visibility = this.modifiers.getVisibility()
	)
}

private fun <S : Scope<S>> TypeAliasDeclaration.toSymbol(owner: S): TypeAliasSymbol<S> {
	return TypeAliasSymbol(
		name = this.name.value,
		owner = owner,
		node = this,
		visibility = this.modifiers.getVisibility()
	)
}