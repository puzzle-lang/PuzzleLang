package puzzle.core.frontend.parser.matcher.declaration.toplevel

import puzzle.core.frontend.ast.declaration.Declaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.matcher.declaration.DeclarationHeader
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.parameter.context.ContextTarget
import puzzle.core.frontend.parser.parser.parameter.type.TypeTarget

sealed interface DeclarationMatcher<out D : Declaration> {
	
	companion object {
		
		val matchers = arrayOf(
			FunDeclarationMatcher,
			PropertyDeclarationMatcher,
			ClassDeclarationMatcher,
			ObjectDeclarationMatcher,
			TraitDeclarationMatcher,
			MixinDeclarationMatcher,
			StructDeclarationMatcher,
			EnumDeclarationMatcher,
			AnnotationDeclarationMatcher,
			ExtensionDeclarationMatcher,
			TypeAliasDeclarationMatcher
		)
	}
	
	val typeTarget: TypeTarget
	
	val modifierTarget: ModifierTarget
	
	val contextTarget: ContextTarget
	
	context(cursor: PzlTokenCursor)
	fun match(): Boolean
	
	context(_: FileContext, cursor: PzlTokenCursor)
	fun parse(header: DeclarationHeader, start: SourceLocation): D
}