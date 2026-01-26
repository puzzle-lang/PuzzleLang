package puzzle.frontend.parser.dispatcher.declaration.member

import puzzle.ast.declaration.StructDeclaration
import puzzle.context.FileContext
import puzzle.base.location.SourceLocation
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.frontend.parser.parser.ModifierTarget
import puzzle.frontend.parser.parser.declaration.parseStructDeclaration

object MemberStructDeclarationDispatcher : MemberDeclarationDispatcher<StructDeclaration> {
	
	override val target = DeclarationTarget.STRUCT
	
	override val modifierTarget = ModifierTarget.MEMBER_STRUCT
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): StructDeclaration {
		return parseStructDeclaration(meta, start)
	}
}