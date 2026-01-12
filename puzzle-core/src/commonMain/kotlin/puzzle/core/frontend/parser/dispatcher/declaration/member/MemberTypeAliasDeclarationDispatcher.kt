package puzzle.core.frontend.parser.dispatcher.declaration.member

import puzzle.core.frontend.ast.declaration.TypeAliasDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parseTypeAliasDeclaration

object MemberTypeAliasDeclarationDispatcher : MemberDeclarationDispatcher<TypeAliasDeclaration> {
	
	override val target = DeclarationTarget.TYPEALIAS
	
	override val modifierTarget = ModifierTarget.MEMBER_TYPEALIAS
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): TypeAliasDeclaration {
		return parseTypeAliasDeclaration(meta, start)
	}
}