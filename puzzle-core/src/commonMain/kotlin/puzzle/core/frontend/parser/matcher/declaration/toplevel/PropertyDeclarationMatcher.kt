package puzzle.core.frontend.parser.matcher.declaration.toplevel

import puzzle.core.frontend.ast.declaration.PropertyDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.matcher.declaration.DeclarationHeader
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parsePropertyDeclaration
import puzzle.core.frontend.parser.parser.parameter.context.ContextTarget
import puzzle.core.frontend.parser.parser.parameter.type.TypeTarget
import puzzle.core.frontend.token.kinds.ModifierKind.VAL
import puzzle.core.frontend.token.kinds.ModifierKind.VAR

object PropertyDeclarationMatcher : DeclarationMatcher<PropertyDeclaration> {
	
	override val typeTarget = TypeTarget.PROPERTY
	
	override val modifierTarget = ModifierTarget.PROPERTY
	
	override val contextTarget = ContextTarget.PROPERTY
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match { it.kind == VAR || it.kind == VAL }
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(header: DeclarationHeader, start: SourceLocation): PropertyDeclaration {
		return parsePropertyDeclaration(header, start)
	}
}