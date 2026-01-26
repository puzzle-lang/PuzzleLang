package puzzle.frontend.parser.dispatcher.declaration.member

import puzzle.ast.declaration.InitDeclaration
import puzzle.context.FileContext
import puzzle.base.location.SourceLocation
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.frontend.parser.parser.ModifierTarget
import puzzle.frontend.parser.parser.declaration.parseInitDeclaration

object MemberInitDeclarationDispatcher : MemberDeclarationDispatcher<InitDeclaration> {
	
	override val target = DeclarationTarget.INIT
	
	override val modifierTarget: ModifierTarget = ModifierTarget.INIT
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): InitDeclaration {
		return parseInitDeclaration(meta, start)
	}
}