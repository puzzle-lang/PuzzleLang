package puzzle.core.frontend.parser.dispatcher.declaration.toplevel

import puzzle.core.frontend.ast.declaration.StructDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parseStructDeclaration

object StructDeclarationDispatcher : DeclarationDispatcher<StructDeclaration> {
	
	override val target = DeclarationTarget.STRUCT
	
	override val modifierTarget = ModifierTarget.STRUCT
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): StructDeclaration {
		return parseStructDeclaration(meta, start)
	}
}