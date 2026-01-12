package puzzle.core.frontend.parser.dispatcher.declaration.member

import puzzle.core.frontend.ast.declaration.CtorDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parseCtorDeclaration

object MemberCtorDeclarationDispatcher : MemberDeclarationDispatcher<CtorDeclaration> {
	
	override val target = DeclarationTarget.CTOR
	
	override val modifierTarget = ModifierTarget.CTOR
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): CtorDeclaration {
		return parseCtorDeclaration(meta, start)
	}
}