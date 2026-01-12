package puzzle.core.frontend.parser.matcher.declaration.member

import puzzle.core.frontend.ast.declaration.ObjectDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.matcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.matcher.declaration.DeclarationTarget
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parseObjectDeclaration
import puzzle.core.frontend.token.kinds.DeclarationKind.OBJECT

object MemberObjectDeclarationMatcher : MemberDeclarationMatcher<ObjectDeclaration> {
	
	override val target = DeclarationTarget.OBJECT
	
	override val modifierTarget = ModifierTarget.MEMBER_OBJECT
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(OBJECT)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): ObjectDeclaration {
		return parseObjectDeclaration(meta, start, isTopLevel = false)
	}
}