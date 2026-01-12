package puzzle.core.frontend.parser.dispatcher.declaration.member

import puzzle.core.frontend.ast.declaration.InitDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parseInitDeclaration

object MemberInitDeclarationDispatcher : MemberDeclarationDispatcher<InitDeclaration> {
	
	override val target = DeclarationTarget.FUN
	
	override val modifierTarget: ModifierTarget = ModifierTarget.INIT
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): InitDeclaration {
		return parseInitDeclaration(meta, start)
	}
}