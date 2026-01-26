package puzzle.frontend.parser.dispatcher.declaration.member

import puzzle.ast.declaration.EnumDeclaration
import puzzle.context.FileContext
import puzzle.base.location.SourceLocation
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.frontend.parser.parser.ModifierTarget
import puzzle.frontend.parser.parser.declaration.parseEnumDeclaration

object MemberEnumDeclarationDispatcher : MemberDeclarationDispatcher<EnumDeclaration> {
	
	override val target = DeclarationTarget.ENUM
	
	override val modifierTarget = ModifierTarget.MEMBER_ENUM
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): EnumDeclaration {
		return parseEnumDeclaration(meta, start)
	}
}