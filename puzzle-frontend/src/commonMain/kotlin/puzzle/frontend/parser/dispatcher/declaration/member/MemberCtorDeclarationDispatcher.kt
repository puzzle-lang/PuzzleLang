package puzzle.frontend.parser.dispatcher.declaration.member

import puzzle.ast.declaration.CtorDeclaration
import puzzle.core.context.FileContext
import puzzle.core.location.SourceLocation
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.frontend.parser.parser.ModifierTarget
import puzzle.frontend.parser.parser.declaration.parseCtorDeclaration

object MemberCtorDeclarationDispatcher : MemberDeclarationDispatcher<CtorDeclaration> {
	
	override val target = DeclarationTarget.CTOR
	
	override val modifierTarget = ModifierTarget.CTOR
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): CtorDeclaration {
		return parseCtorDeclaration(meta, start)
	}
}