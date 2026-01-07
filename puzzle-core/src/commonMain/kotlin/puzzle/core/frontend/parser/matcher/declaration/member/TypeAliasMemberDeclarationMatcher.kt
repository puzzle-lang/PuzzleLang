package puzzle.core.frontend.parser.matcher.declaration.member

import puzzle.core.frontend.model.PzlContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.ast.declaration.TypeAliasDeclaration
import puzzle.core.frontend.parser.matcher.declaration.DeclarationHeader
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parseTypeAliasDeclaration
import puzzle.core.frontend.parser.parser.parameter.context.ContextTarget
import puzzle.core.frontend.parser.parser.parameter.type.TypeTarget
import puzzle.core.frontend.token.kinds.DeclarationKind.TYPEALIAS

object TypeAliasMemberDeclarationMatcher : MemberDeclarationMatcher<TypeAliasDeclaration> {
	
	override val typeTarget = TypeTarget.TYPEALIAS
	
	override val modifierTarget = ModifierTarget.MEMBER_TYPEALIAS
	
	override val contextTarget = ContextTarget.TYPEALIAS
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(TYPEALIAS)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(header: DeclarationHeader, start: SourceLocation): TypeAliasDeclaration {
		return parseTypeAliasDeclaration(header, start)
	}
}