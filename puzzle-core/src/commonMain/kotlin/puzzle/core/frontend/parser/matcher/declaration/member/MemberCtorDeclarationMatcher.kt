package puzzle.core.frontend.parser.matcher.declaration.member

import puzzle.core.frontend.ast.declaration.CtorDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.matcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.matcher.declaration.DeclarationTarget
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parseCtorDeclaration
import puzzle.core.frontend.token.kinds.DeclarationKind

object MemberCtorDeclarationMatcher : MemberDeclarationMatcher<CtorDeclaration> {
	
	override val target = DeclarationTarget.CTOR
	
	override val modifierTarget = ModifierTarget.CTOR
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(DeclarationKind.CTOR)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): CtorDeclaration {
		return parseCtorDeclaration(meta, start)
	}
}