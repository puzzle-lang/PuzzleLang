package puzzle.frontend.parser.dispatcher.declaration.toplevel

import puzzle.ast.declaration.ExtensionDeclaration
import puzzle.context.FileContext
import puzzle.base.location.SourceLocation
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.frontend.parser.parser.ModifierTarget
import puzzle.frontend.parser.parser.declaration.parseExtensionDeclaration

object ExtensionDeclarationDispatcher : DeclarationDispatcher<ExtensionDeclaration> {
	
	override val target = DeclarationTarget.EXTENSION
	
	override val modifierTarget = ModifierTarget.EXTENSION
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): ExtensionDeclaration {
		return parseExtensionDeclaration(meta, start)
	}
}