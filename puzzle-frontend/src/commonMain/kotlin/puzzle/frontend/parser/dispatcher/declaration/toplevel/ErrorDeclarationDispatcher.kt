package puzzle.frontend.parser.dispatcher.declaration.toplevel

import puzzle.ast.declaration.ErrorDeclaration
import puzzle.core.context.FileContext
import puzzle.core.location.SourceLocation
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.frontend.parser.parser.ModifierTarget
import puzzle.frontend.parser.parser.declaration.parseErrorDeclaration

object ErrorDeclarationDispatcher : DeclarationDispatcher<ErrorDeclaration> {
	
	override val target = DeclarationTarget.ERROR
	
	override val modifierTarget = ModifierTarget.MEMBER_ERROR
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): ErrorDeclaration {
		return parseErrorDeclaration(meta, start)
	}
}