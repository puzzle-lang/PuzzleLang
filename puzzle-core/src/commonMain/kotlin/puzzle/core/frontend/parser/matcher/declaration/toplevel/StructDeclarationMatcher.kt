package puzzle.core.frontend.parser.matcher.declaration.toplevel

import puzzle.core.frontend.ast.declaration.StructDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.matcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.matcher.declaration.DeclarationTarget
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parseStructDeclaration
import puzzle.core.frontend.token.kinds.DeclarationKind.STRUCT

object StructDeclarationMatcher : DeclarationMatcher<StructDeclaration> {
	
	override val target = DeclarationTarget.STRUCT
	
	override val modifierTarget = ModifierTarget.STRUCT
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(STRUCT)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): StructDeclaration {
		return parseStructDeclaration(meta, start)
	}
}