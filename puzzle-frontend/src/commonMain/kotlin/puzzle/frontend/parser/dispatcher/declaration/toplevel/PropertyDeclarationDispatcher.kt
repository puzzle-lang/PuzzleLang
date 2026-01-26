package puzzle.frontend.parser.dispatcher.declaration.toplevel

import puzzle.ast.declaration.PropertyDeclaration
import puzzle.context.FileContext
import puzzle.base.location.SourceLocation
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.frontend.parser.parser.ModifierTarget
import puzzle.frontend.parser.parser.declaration.parsePropertyDeclaration

object PropertyDeclarationDispatcher : DeclarationDispatcher<PropertyDeclaration> {
	
	override val target = DeclarationTarget.PROPERTY
	
	override val modifierTarget = ModifierTarget.PROPERTY
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): PropertyDeclaration {
		return parsePropertyDeclaration(meta, start, isTopLevel = true)
	}
}