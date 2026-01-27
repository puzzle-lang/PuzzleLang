package puzzle.frontend.parser.dispatcher.declaration.member

import puzzle.ast.declaration.AnnotationDeclaration
import puzzle.core.context.FileContext
import puzzle.core.location.SourceLocation
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.frontend.parser.parser.ModifierTarget
import puzzle.frontend.parser.parser.declaration.parseAnnotationDeclaration

object MemberAnnotationDeclarationDispatcher : MemberDeclarationDispatcher<AnnotationDeclaration> {
	
	override val target = DeclarationTarget.ANNOTATION
	
	override val modifierTarget = ModifierTarget.MEMBER_ANNOTATION
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): AnnotationDeclaration {
		return parseAnnotationDeclaration(meta, start)
	}
}