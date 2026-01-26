package puzzle.frontend.parser.dispatcher.declaration.toplevel

import puzzle.ast.declaration.MixinDeclaration
import puzzle.context.FileContext
import puzzle.base.location.SourceLocation
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.frontend.parser.parser.ModifierTarget
import puzzle.frontend.parser.parser.declaration.parseMixinDeclaration

object MixinDeclarationDispatcher : DeclarationDispatcher<MixinDeclaration> {
	
	override val target = DeclarationTarget.MIXIN
	
	override val modifierTarget = ModifierTarget.MIXIN
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): MixinDeclaration {
		return parseMixinDeclaration(meta, start)
	}
}