package puzzle.core.frontend.parser.matcher.declaration.toplevel

import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.ast.declaration.MixinDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.matcher.declaration.DeclarationHeader
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parseMixinDeclaration
import puzzle.core.frontend.parser.parser.parameter.context.ContextTarget
import puzzle.core.frontend.parser.parser.parameter.type.TypeTarget
import puzzle.core.frontend.token.kinds.DeclarationKind.MIXIN

object MixinDeclarationMatcher : DeclarationMatcher<MixinDeclaration> {
	
	override val typeTarget = TypeTarget.MIXIN
	
	override val modifierTarget = ModifierTarget.MIXIN
	
	override val contextTarget = ContextTarget.MIXIN
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(MIXIN)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(header: DeclarationHeader, start: SourceLocation): MixinDeclaration {
		return parseMixinDeclaration(header, start)
	}
}