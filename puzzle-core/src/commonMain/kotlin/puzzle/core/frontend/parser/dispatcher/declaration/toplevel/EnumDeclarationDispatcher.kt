package puzzle.core.frontend.parser.dispatcher.declaration.toplevel

import puzzle.core.frontend.ast.declaration.EnumDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parseEnumDeclaration

object EnumDeclarationDispatcher : DeclarationDispatcher<EnumDeclaration> {
	
	override val target = DeclarationTarget.ENUM
	
	override val modifierTarget = ModifierTarget.ENUM
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): EnumDeclaration {
		return parseEnumDeclaration(meta, start)
	}
}