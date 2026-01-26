package puzzle.frontend.parser.dispatcher.declaration.member

import puzzle.ast.declaration.ClassDeclaration
import puzzle.context.FileContext
import puzzle.base.location.SourceLocation
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.frontend.parser.parser.ModifierTarget
import puzzle.frontend.parser.parser.declaration.parseClassDeclaration

object MemberClassDeclarationDispatcher : MemberDeclarationDispatcher<ClassDeclaration> {
	
	override val target = DeclarationTarget.CLASS
	
	override val modifierTarget = ModifierTarget.MEMBER_CLASS
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): ClassDeclaration {
		return parseClassDeclaration(meta, start)
	}
}