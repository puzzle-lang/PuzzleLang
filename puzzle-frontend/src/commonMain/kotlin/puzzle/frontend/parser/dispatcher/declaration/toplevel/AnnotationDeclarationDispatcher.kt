package puzzle.frontend.parser.dispatcher.declaration.toplevel

import puzzle.ast.declaration.AnnotationDeclaration
import puzzle.context.FileContext
import puzzle.base.location.SourceLocation
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.frontend.parser.parser.ModifierTarget
import puzzle.frontend.parser.parser.declaration.parseAnnotationDeclaration

object AnnotationDeclarationDispatcher : DeclarationDispatcher<AnnotationDeclaration> {
	
	override val target = DeclarationTarget.ANNOTATION
	
	override val modifierTarget = ModifierTarget.ANNOTATION
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): AnnotationDeclaration {
		return parseAnnotationDeclaration(meta, start)
	}
}