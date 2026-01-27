package puzzle.frontend.parser.dispatcher.declaration.toplevel

import puzzle.ast.declaration.TopLevelAllowedDeclaration
import puzzle.core.context.FileContext
import puzzle.core.location.SourceLocation
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.frontend.parser.parser.ModifierTarget

sealed interface DeclarationDispatcher<out D : TopLevelAllowedDeclaration> {
	
	val target: DeclarationTarget
	
	val modifierTarget: ModifierTarget
	
	context(_: FileContext, cursor: PzlTokenCursor)
	fun parse(meta: DeclarationMeta, start: SourceLocation): D
}