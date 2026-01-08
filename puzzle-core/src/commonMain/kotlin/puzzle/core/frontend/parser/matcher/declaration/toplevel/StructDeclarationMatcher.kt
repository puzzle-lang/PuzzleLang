package puzzle.core.frontend.parser.matcher.declaration.toplevel

import puzzle.core.frontend.ast.declaration.StructDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.matcher.declaration.DeclarationHeader
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parseStructDeclaration
import puzzle.core.frontend.parser.parser.parameter.context.ContextTarget
import puzzle.core.frontend.parser.parser.parameter.type.TypeTarget
import puzzle.core.frontend.token.kinds.DeclarationKind.STRUCT

object StructDeclarationMatcher : DeclarationMatcher<StructDeclaration> {
	
	override val typeTarget = TypeTarget.STRUCT
	
	override val modifierTarget = ModifierTarget.STRUCT
	
	override val contextTarget = ContextTarget.STRUCT
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(STRUCT)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(header: DeclarationHeader, start: SourceLocation): StructDeclaration {
		return parseStructDeclaration(header, start)
	}
}