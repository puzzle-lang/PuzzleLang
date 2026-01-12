package puzzle.core.frontend.parser.matcher.declaration.member

import puzzle.core.frontend.ast.declaration.ClassDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.matcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.matcher.declaration.DeclarationTarget
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parseClassDeclaration
import puzzle.core.frontend.token.kinds.DeclarationKind.CLASS

object MemberClassDeclarationMatcher : MemberDeclarationMatcher<ClassDeclaration> {
	
	override val target = DeclarationTarget.CLASS
	
	override val modifierTarget = ModifierTarget.MEMBER_CLASS
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(CLASS)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): ClassDeclaration {
		return parseClassDeclaration(meta, start)
	}
}