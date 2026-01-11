package puzzle.core.frontend.parser.matcher.declaration.member

import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.ast.declaration.ObjectDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.matcher.declaration.DeclarationHeader
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parseObjectDeclaration
import puzzle.core.frontend.parser.parser.parameter.context.ContextTarget
import puzzle.core.frontend.parser.parser.parameter.type.TypeTarget
import puzzle.core.frontend.token.kinds.DeclarationKind.OBJECT

object MemberObjectDeclarationMatcher : MemberDeclarationMatcher<ObjectDeclaration> {
	
	override val typeTarget = TypeTarget.OBJECT
	
	override val modifierTarget = ModifierTarget.MEMBER_OBJECT
	
	override val contextTarget = ContextTarget.OBJECT
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(OBJECT)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(header: DeclarationHeader, start: SourceLocation): ObjectDeclaration {
		return parseObjectDeclaration(header, start, isTopLevel = false)
	}
}