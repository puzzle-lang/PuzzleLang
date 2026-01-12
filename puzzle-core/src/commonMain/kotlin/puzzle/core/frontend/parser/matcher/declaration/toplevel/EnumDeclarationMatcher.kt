package puzzle.core.frontend.parser.matcher.declaration.toplevel

import puzzle.core.frontend.ast.declaration.EnumDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.matcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.matcher.declaration.DeclarationTarget
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parseEnumDeclaration
import puzzle.core.frontend.token.kinds.DeclarationKind.ENUM

object EnumDeclarationMatcher : DeclarationMatcher<EnumDeclaration> {
	
	override val target = DeclarationTarget.ENUM
	override val modifierTarget = ModifierTarget.ENUM
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(ENUM)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): EnumDeclaration {
		return parseEnumDeclaration(meta, start)
	}
}