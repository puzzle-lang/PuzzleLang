package puzzle.frontend.parser.dispatcher.declaration.member

import puzzle.ast.declaration.PropertyDeclaration
import puzzle.core.context.FileContext
import puzzle.core.location.SourceLocation
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.frontend.parser.parser.ModifierTarget
import puzzle.frontend.parser.parser.declaration.parsePropertyDeclaration

object MemberPropertyDeclarationDispatcher : MemberDeclarationDispatcher<PropertyDeclaration> {
	
	override val target = DeclarationTarget.PROPERTY
	
	override val modifierTarget = ModifierTarget.MEMBER_PROPERTY
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): PropertyDeclaration {
		return parsePropertyDeclaration(meta, start, isTopLevel = false)
	}
}