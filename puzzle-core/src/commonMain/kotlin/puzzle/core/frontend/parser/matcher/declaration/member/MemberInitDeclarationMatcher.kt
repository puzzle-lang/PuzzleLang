package puzzle.core.frontend.parser.matcher.declaration.member

import puzzle.core.frontend.ast.declaration.InitDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.matcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.matcher.declaration.DeclarationTarget
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parseInitDeclaration
import puzzle.core.frontend.token.kinds.ContextualKind.INIT

object MemberInitDeclarationMatcher : MemberDeclarationMatcher<InitDeclaration> {
	
	override val target = DeclarationTarget.FUN
	
	override val modifierTarget: ModifierTarget = ModifierTarget.INIT
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(INIT)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): InitDeclaration {
		return parseInitDeclaration(meta, start)
	}
}