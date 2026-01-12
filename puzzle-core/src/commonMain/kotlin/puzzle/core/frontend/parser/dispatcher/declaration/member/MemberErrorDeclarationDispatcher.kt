package puzzle.core.frontend.parser.dispatcher.declaration.member

import puzzle.core.frontend.ast.declaration.ErrorDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parseErrorDeclaration

object MemberErrorDeclarationDispatcher : MemberDeclarationDispatcher<ErrorDeclaration> {
	
	override val target = DeclarationTarget.ERROR
	
	override val modifierTarget = ModifierTarget.ERROR
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): ErrorDeclaration {
		return parseErrorDeclaration(meta, start)
	}
}