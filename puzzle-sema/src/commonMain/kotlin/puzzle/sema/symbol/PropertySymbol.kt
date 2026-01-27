package puzzle.sema.symbol

import puzzle.ast.declaration.Property
import puzzle.ast.declaration.PropertyGetter
import puzzle.ast.declaration.PropertySetter
import puzzle.ast.expression.Identifier
import puzzle.core.context.FileContext
import puzzle.sema.scope.BlockScope
import puzzle.sema.scope.PzlScope
import puzzle.token.kinds.Visibility

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