package puzzle.frontend.parser.dispatcher.declaration.member

import puzzle.ast.declaration.ObjectDeclaration
import puzzle.context.FileContext
import puzzle.base.location.SourceLocation
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.frontend.parser.parser.ModifierTarget
import puzzle.frontend.parser.parser.declaration.parseObjectDeclaration

object MemberObjectDeclarationDispatcher : MemberDeclarationDispatcher<ObjectDeclaration> {
	
	override val target = DeclarationTarget.OBJECT
	
	override val modifierTarget = ModifierTarget.MEMBER_OBJECT
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): ObjectDeclaration {
		return parseObjectDeclaration(meta, start, isTopLevel = false)
	}
}