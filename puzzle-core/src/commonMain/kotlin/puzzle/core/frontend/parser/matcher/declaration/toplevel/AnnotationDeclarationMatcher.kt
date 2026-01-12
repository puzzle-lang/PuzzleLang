package puzzle.core.frontend.parser.matcher.declaration.toplevel

import puzzle.core.frontend.ast.declaration.AnnotationDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.matcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.matcher.declaration.DeclarationTarget
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parseAnnotationDeclaration
import puzzle.core.frontend.token.kinds.DeclarationKind.ANNOTATION

object AnnotationDeclarationMatcher : DeclarationMatcher<AnnotationDeclaration> {
	
	override val target = DeclarationTarget.ANNOTATION
	
	override val modifierTarget = ModifierTarget.ANNOTATION
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(ANNOTATION)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): AnnotationDeclaration {
		return parseAnnotationDeclaration(meta, start)
	}
}