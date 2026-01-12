package puzzle.core.frontend.parser.dispatcher.declaration.member

import puzzle.core.frontend.ast.declaration.PropertyDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parsePropertyDeclaration

object MemberPropertyDeclarationDispatcher : MemberDeclarationDispatcher<PropertyDeclaration> {
	
	override val target = DeclarationTarget.PROPERTY
	
	override val modifierTarget = ModifierTarget.MEMBER_PROPERTY
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): PropertyDeclaration {
		return parsePropertyDeclaration(meta, start, isTopLevel = false)
	}
}