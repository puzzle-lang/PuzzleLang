package puzzle.core.frontend.parser.dispatcher.declaration.toplevel

import puzzle.core.frontend.ast.declaration.TopLevelAllowedDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.core.frontend.parser.parser.ModifierTarget

sealed interface DeclarationDispatcher<out D : TopLevelAllowedDeclaration> {
	
	val target: DeclarationTarget
	
	val modifierTarget: ModifierTarget
	
	context(_: FileContext, cursor: PzlTokenCursor)
	fun parse(meta: DeclarationMeta, start: SourceLocation): D
}