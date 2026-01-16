package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.Property
import puzzle.core.frontend.ast.declaration.PropertyGetter
import puzzle.core.frontend.ast.declaration.PropertySetter
import puzzle.core.frontend.semantics.scope.BlockScope
import puzzle.core.frontend.semantics.scope.FileContextScope

class PropertySymbol(
	override val name: String,
	override val owner: FileContextScope,
	override val node: Property,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override val kind = PzlSymbolKind.PROPERTY
}

class PropertyGetterSymbol(
	override val name: String,
	override val owner: FileContextScope,
	override val node: PropertyGetter,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override val kind = PzlSymbolKind.PROPERTY_GETTER
	
	override var scope: BlockScope? = null
}

class PropertySetterSymbol(
	override val name: String,
	override val owner: FileContextScope,
	override val node: PropertySetter,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override val kind = PzlSymbolKind.PROPERTY_SETTER
	
	override var scope: BlockScope? = null
}