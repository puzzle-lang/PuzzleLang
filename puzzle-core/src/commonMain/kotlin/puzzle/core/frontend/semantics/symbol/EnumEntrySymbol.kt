package puzzle.core.frontend.semantics.symbol

import puzzle.core.frontend.ast.declaration.EnumEntry
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.semantics.scope.EnumEntryScope
import puzzle.core.frontend.semantics.scope.EnumScope

class EnumEntrySymbol(
	override val name: Identifier,
	override val owner: EnumScope,
	override val node: EnumEntry,
) : PzlSymbol {
	
	override lateinit var scope: EnumEntryScope
	
	override val kind = PzlSymbolKind.ENUM_ENTRY
	
	override val isTypeDeclaration = false
}