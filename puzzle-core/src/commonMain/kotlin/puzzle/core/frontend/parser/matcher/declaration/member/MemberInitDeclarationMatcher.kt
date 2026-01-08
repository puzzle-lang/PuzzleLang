package puzzle.core.frontend.parser.matcher.declaration.member

import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.ast.declaration.InitDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.matcher.declaration.DeclarationHeader
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parseInitDeclaration
import puzzle.core.frontend.parser.parser.parameter.context.ContextTarget
import puzzle.core.frontend.parser.parser.parameter.type.TypeTarget
import puzzle.core.frontend.token.kinds.ContextualKind.INIT

object MemberInitDeclarationMatcher : MemberDeclarationMatcher<InitDeclaration> {
	
	override val typeTarget: TypeTarget = TypeTarget.INIT
	
	override val modifierTarget: ModifierTarget = ModifierTarget.INIT
	
	override val contextTarget = ContextTarget.INIT
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(INIT)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(header: DeclarationHeader, start: SourceLocation): InitDeclaration {
		return parseInitDeclaration(header, start)
	}
}