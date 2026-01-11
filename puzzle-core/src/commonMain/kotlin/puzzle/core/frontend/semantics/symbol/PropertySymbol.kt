package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.Property
import puzzle.core.frontend.ast.declaration.PropertyGetter
import puzzle.core.frontend.ast.declaration.PropertySetter
import puzzle.core.frontend.semantics.scope.Scope

class PropertySymbol(
	override val name: String,
	override val owner: Scope,
	override val node: Property,
	override val visibility: Visibility,
) : Symbol {
	
	override val kind = SymbolKind.PROPERTY
}

class PropertyGetterSymbol(
	override val name: String,
	override val owner: Scope,
	override val node: PropertyGetter,
	override val visibility: Visibility,
) : Symbol {
	
	override val kind = SymbolKind.PROPERTY_GETTER
}

class PropertySetterSymbol(
	override val name: String,
	override val owner: Scope,
	override val node: PropertySetter,
	override val visibility: Visibility,
) : Symbol {
	
	override val kind = SymbolKind.PROPERTY_SETTER
}