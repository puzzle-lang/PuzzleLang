package puzzle.core.frontend.parser.dispatcher.declaration.toplevel

import puzzle.core.frontend.ast.declaration.FunDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parseFunDeclaration

object FunDeclarationDispatcher : DeclarationDispatcher<FunDeclaration> {
	
	override val target = DeclarationTarget.FUN
	
	override val modifierTarget = ModifierTarget.FUN
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): FunDeclaration {
		return parseFunDeclaration(meta, start)
	}
}