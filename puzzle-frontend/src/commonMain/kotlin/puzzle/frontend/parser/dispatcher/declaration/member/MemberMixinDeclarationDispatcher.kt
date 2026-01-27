package puzzle.frontend.parser.dispatcher.declaration.member

import puzzle.ast.declaration.MixinDeclaration
import puzzle.core.context.FileContext
import puzzle.core.location.SourceLocation
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.frontend.parser.parser.ModifierTarget
import puzzle.frontend.parser.parser.declaration.parseMixinDeclaration

object MemberMixinDeclarationDispatcher : MemberDeclarationDispatcher<MixinDeclaration> {
	
	override val target = DeclarationTarget.MIXIN
	
	override val modifierTarget = ModifierTarget.MEMBER_MIXIN
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): MixinDeclaration {
		return parseMixinDeclaration(meta, start)
	}
}