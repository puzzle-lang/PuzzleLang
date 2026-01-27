package puzzle.frontend.parser.dispatcher.declaration.toplevel

import puzzle.ast.declaration.TypeAliasDeclaration
import puzzle.core.context.FileContext
import puzzle.core.location.SourceLocation
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.frontend.parser.dispatcher.declaration.DeclarationTarget
import puzzle.frontend.parser.parser.ModifierTarget
import puzzle.frontend.parser.parser.declaration.parseTypeAliasDeclaration

object TypeAliasDeclarationDispatcher : DeclarationDispatcher<TypeAliasDeclaration> {
	
	override val target = DeclarationTarget.TYPEALIAS
	
	override val modifierTarget = ModifierTarget.TYPEALIAS
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): TypeAliasDeclaration {
		return parseTypeAliasDeclaration(meta, start)
	}
}