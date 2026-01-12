package puzzle.core.frontend.parser.dispatcher.declaration.member

import puzzle.core.frontend.ast.declaration.ObjectDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parseObjectDeclaration

object MemberObjectDeclarationDispatcher : MemberDeclarationDispatcher<ObjectDeclaration> {
	
	override val target = DeclarationTarget.OBJECT
	
	override val modifierTarget = ModifierTarget.MEMBER_OBJECT
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): ObjectDeclaration {
		return parseObjectDeclaration(meta, start, isTopLevel = false)
	}
}