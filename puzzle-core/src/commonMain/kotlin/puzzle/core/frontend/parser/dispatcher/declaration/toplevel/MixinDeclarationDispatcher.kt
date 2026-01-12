package puzzle.core.frontend.parser.dispatcher.declaration.toplevel

import puzzle.core.frontend.ast.declaration.MixinDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parseMixinDeclaration

object MixinDeclarationDispatcher : DeclarationDispatcher<MixinDeclaration> {
	
	override val target = DeclarationTarget.MIXIN
	
	override val modifierTarget = ModifierTarget.MIXIN
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): MixinDeclaration {
		return parseMixinDeclaration(meta, start)
	}
}