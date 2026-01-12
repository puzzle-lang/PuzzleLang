package puzzle.core.frontend.parser.dispatcher.declaration.member

import puzzle.core.frontend.ast.declaration.ExtensionDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parseExtensionDeclaration

object MemberExtensionDeclarationDispatcher : MemberDeclarationDispatcher<ExtensionDeclaration> {
	
	override val target = DeclarationTarget.EXTENSION
	
	override val modifierTarget = ModifierTarget.MEMBER_EXTENSION
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): ExtensionDeclaration {
		return parseExtensionDeclaration(meta, start)
	}
}