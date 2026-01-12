package puzzle.core.frontend.parser.matcher.declaration.member

import puzzle.core.frontend.ast.declaration.MixinDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.matcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.matcher.declaration.DeclarationTarget
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parseMixinDeclaration
import puzzle.core.frontend.token.kinds.DeclarationKind.MIXIN

object MemberMixinDeclarationMatcher : MemberDeclarationMatcher<MixinDeclaration> {
	
	override val target = DeclarationTarget.MIXIN
	
	override val modifierTarget = ModifierTarget.MEMBER_MIXIN
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(MIXIN)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): MixinDeclaration {
		return parseMixinDeclaration(meta, start)
	}
}