package puzzle.frontend.parser.dispatcher.declaration.member

import puzzle.ast.declaration.Declaration
import puzzle.context.FileContext
import puzzle.base.location.SourceLocation
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.frontend.parser.parser.ModifierTarget

sealed interface MemberDeclarationDispatcher<out D : Declaration> {
	
	val target: DeclarationTarget
	
	val modifierTarget: ModifierTarget
	
	context(_: FileContext, cursor: PzlTokenCursor)
	fun parse(meta: DeclarationMeta, start: SourceLocation): D
}