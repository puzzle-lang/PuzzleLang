package puzzle.core.frontend.parser.dispatcher.declaration.toplevel

import puzzle.core.frontend.ast.declaration.AnnotationDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parseAnnotationDeclaration

object AnnotationDeclarationDispatcher : DeclarationDispatcher<AnnotationDeclaration> {
	
	override val target = DeclarationTarget.ANNOTATION
	
	override val modifierTarget = ModifierTarget.ANNOTATION
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): AnnotationDeclaration {
		return parseAnnotationDeclaration(meta, start)
	}
}