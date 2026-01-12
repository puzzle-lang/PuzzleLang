package puzzle.core.frontend.parser.dispatcher.declaration.member

import puzzle.core.frontend.ast.declaration.Declaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.core.frontend.parser.parser.ModifierTarget

sealed interface MemberDeclarationDispatcher<out D : Declaration> {
	
	val target: DeclarationTarget
	
	val modifierTarget: ModifierTarget
	
	context(_: FileContext, cursor: PzlTokenCursor)
	fun parse(meta: DeclarationMeta, start: SourceLocation): D
}