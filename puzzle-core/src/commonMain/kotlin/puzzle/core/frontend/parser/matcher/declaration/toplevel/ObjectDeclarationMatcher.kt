package puzzle.core.frontend.parser.matcher.declaration.toplevel

import puzzle.core.frontend.ast.declaration.ObjectDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.matcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.matcher.declaration.DeclarationTarget
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parseObjectDeclaration
import puzzle.core.frontend.token.kinds.DeclarationKind.OBJECT

object ObjectDeclarationMatcher : DeclarationMatcher<ObjectDeclaration> {
	
	override val target = DeclarationTarget.OBJECT
	
	override val modifierTarget = ModifierTarget.OBJECT
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(OBJECT)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): ObjectDeclaration {
		return parseObjectDeclaration(meta, start, isTopLevel = true)
	}
}