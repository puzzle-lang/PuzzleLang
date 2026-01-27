package puzzle.frontend.parser.dispatcher.declaration.member

import puzzle.ast.declaration.TraitDeclaration
import puzzle.core.context.FileContext
import puzzle.core.location.SourceLocation
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.frontend.parser.parser.ModifierTarget
import puzzle.frontend.parser.parser.declaration.parseTraitDeclaration

object MemberTraitDeclarationDispatcher : MemberDeclarationDispatcher<TraitDeclaration> {
	
	override val target = DeclarationTarget.TRAIT
	
	override val modifierTarget = ModifierTarget.MEMBER_TRAIT
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): TraitDeclaration {
		return parseTraitDeclaration(meta, start)
	}
}