package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.Property
import puzzle.core.frontend.ast.declaration.PropertyGetter
import puzzle.core.frontend.ast.declaration.PropertySetter
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.semantics.scope.BlockScope
import puzzle.core.frontend.semantics.scope.PzlScope

class PropertySymbol(
	override val name: Identifier,
	override val owner: PzlScope<FileContext>,
	override val node: Property,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override val kind = PzlSymbolKind.PROPERTY
	
	override val isTypeDeclaration = false
}

class PropertyGetterSymbol(
	override val name: Identifier,
	override val owner: PzlScope<FileContext>,
	override val node: PropertyGetter,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override lateinit var scope: BlockScope
	
	override val kind = PzlSymbolKind.PROPERTY_GETTER
	
	override val isTypeDeclaration = false
}

class PropertySetterSymbol(
	override val name: Identifier,
	override val owner: PzlScope<FileContext>,
	override val node: PropertySetter,
	override val visibility: Visibility,
) : PzlSymbol {
	
	override lateinit var scope: BlockScope
	
	override val kind = PzlSymbolKind.PROPERTY_SETTER
	
	override val isTypeDeclaration = false
}