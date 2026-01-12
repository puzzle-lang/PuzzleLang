package puzzle.core.frontend.parser.matcher.declaration.member

import puzzle.core.frontend.ast.declaration.ExtensionDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.matcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.matcher.declaration.DeclarationTarget
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parseExtensionDeclaration
import puzzle.core.frontend.token.kinds.DeclarationKind.EXTENSION

object MemberExtensionDeclarationMatcher : MemberDeclarationMatcher<ExtensionDeclaration> {
	
	override val target = DeclarationTarget.EXTENSION
	
	override val modifierTarget = ModifierTarget.MEMBER_EXTENSION
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(EXTENSION)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): ExtensionDeclaration {
		return parseExtensionDeclaration(meta, start)
	}
}