package puzzle.core.frontend.parser.matcher.declaration.member

import puzzle.core.frontend.ast.declaration.TypeAliasDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.matcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.matcher.declaration.DeclarationTarget
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parseTypeAliasDeclaration
import puzzle.core.frontend.token.kinds.DeclarationKind.TYPEALIAS

object MemberTypeAliasDeclarationMatcher : MemberDeclarationMatcher<TypeAliasDeclaration> {
	
	override val target = DeclarationTarget.TYPEALIAS
	
	override val modifierTarget = ModifierTarget.MEMBER_TYPEALIAS
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(TYPEALIAS)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): TypeAliasDeclaration {
		return parseTypeAliasDeclaration(meta, start)
	}
}