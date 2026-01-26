package puzzle.sema.symbol

import puzzle.ast.declaration.EnumEntry
import puzzle.ast.expression.Identifier
import puzzle.sema.scope.EnumEntryScope
import puzzle.sema.scope.EnumScope

class EnumEntrySymbol(
	override val name: Identifier,
	override val owner: EnumScope,
	override val node: EnumEntry,
) : PzlSymbol {
	
	override lateinit var scope: EnumEntryScope
	
	override val kind = PzlSymbolKind.ENUM_ENTRY
	
	override val isTypeDeclaration = false
}