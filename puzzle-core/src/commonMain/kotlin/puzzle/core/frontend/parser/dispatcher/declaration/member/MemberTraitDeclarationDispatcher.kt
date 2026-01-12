package puzzle.core.frontend.parser.dispatcher.declaration.member

import puzzle.core.frontend.ast.declaration.TraitDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parseTraitDeclaration

object MemberTraitDeclarationDispatcher : MemberDeclarationDispatcher<TraitDeclaration> {
	
	override val target = DeclarationTarget.TRAIT
	
	override val modifierTarget = ModifierTarget.MEMBER_TRAIT
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): TraitDeclaration {
		return parseTraitDeclaration(meta, start)
	}
}