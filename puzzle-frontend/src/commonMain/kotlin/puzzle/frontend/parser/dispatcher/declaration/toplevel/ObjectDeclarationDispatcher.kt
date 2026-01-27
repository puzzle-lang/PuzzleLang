package puzzle.frontend.parser.dispatcher.declaration.toplevel

import puzzle.ast.declaration.ObjectDeclaration
import puzzle.core.context.FileContext
import puzzle.core.location.SourceLocation
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.frontend.parser.parser.ModifierTarget
import puzzle.frontend.parser.parser.declaration.parseObjectDeclaration

object ObjectDeclarationDispatcher : DeclarationDispatcher<ObjectDeclaration> {
	
	override val target = DeclarationTarget.OBJECT
	
	override val modifierTarget = ModifierTarget.OBJECT
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): ObjectDeclaration {
		return parseObjectDeclaration(meta, start, isTopLevel = true)
	}
}