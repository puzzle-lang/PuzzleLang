package puzzle.core.frontend.parser.matcher.declaration.toplevel

import puzzle.core.frontend.ast.declaration.FunDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.matcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.matcher.declaration.DeclarationTarget
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parseFunDeclaration
import puzzle.core.frontend.token.kinds.DeclarationKind.FUN

object FunDeclarationMatcher : DeclarationMatcher<FunDeclaration> {
	
	override val target = DeclarationTarget.FUN
	
	override val modifierTarget = ModifierTarget.FUN
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(FUN)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): FunDeclaration {
		return parseFunDeclaration(meta, start)
	}
}