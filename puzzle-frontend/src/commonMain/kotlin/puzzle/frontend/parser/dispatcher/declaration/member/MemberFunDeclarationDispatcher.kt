package puzzle.frontend.parser.dispatcher.declaration.member

import puzzle.ast.declaration.FunDeclaration
import puzzle.core.context.FileContext
import puzzle.core.location.SourceLocation
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.frontend.parser.parser.ModifierTarget
import puzzle.frontend.parser.parser.declaration.parseFunDeclaration

object MemberFunDeclarationDispatcher : MemberDeclarationDispatcher<FunDeclaration> {
	
	override val target = DeclarationTarget.FUN
	
	override val modifierTarget = ModifierTarget.MEMBER_FUN
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): FunDeclaration {
		return parseFunDeclaration(meta, start)
	}
}