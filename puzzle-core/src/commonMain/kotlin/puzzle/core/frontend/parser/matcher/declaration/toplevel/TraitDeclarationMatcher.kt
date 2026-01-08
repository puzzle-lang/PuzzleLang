package puzzle.core.frontend.parser.matcher.declaration.toplevel

import puzzle.core.frontend.ast.declaration.TraitDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.matcher.declaration.DeclarationHeader
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parseTraitDeclaration
import puzzle.core.frontend.parser.parser.parameter.context.ContextTarget
import puzzle.core.frontend.parser.parser.parameter.type.TypeTarget
import puzzle.core.frontend.token.kinds.DeclarationKind.TRAIT

object TraitDeclarationMatcher : DeclarationMatcher<TraitDeclaration> {
	
	override val typeTarget = TypeTarget.TRAIT
	
	override val modifierTarget = ModifierTarget.TRAIT
	
	override val contextTarget = ContextTarget.TRAIT
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(TRAIT)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(header: DeclarationHeader, start: SourceLocation): TraitDeclaration {
		return parseTraitDeclaration(header, start)
	}
}