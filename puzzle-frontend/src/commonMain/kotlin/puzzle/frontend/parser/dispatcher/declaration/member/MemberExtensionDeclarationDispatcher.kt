package puzzle.frontend.parser.dispatcher.declaration.member

import puzzle.ast.declaration.ExtensionDeclaration
import puzzle.context.FileContext
import puzzle.base.location.SourceLocation
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.frontend.parser.parser.ModifierTarget
import puzzle.frontend.parser.parser.declaration.parseExtensionDeclaration

object MemberExtensionDeclarationDispatcher : MemberDeclarationDispatcher<ExtensionDeclaration> {
	
	override val target = DeclarationTarget.EXTENSION
	
	override val modifierTarget = ModifierTarget.MEMBER_EXTENSION
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): ExtensionDeclaration {
		return parseExtensionDeclaration(meta, start)
	}
}