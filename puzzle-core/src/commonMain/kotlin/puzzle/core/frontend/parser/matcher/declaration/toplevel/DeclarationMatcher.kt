package puzzle.core.frontend.parser.matcher.declaration.toplevel

import puzzle.core.frontend.ast.declaration.Declaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.matcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.matcher.declaration.DeclarationTarget
import puzzle.core.frontend.parser.parser.ModifierTarget

sealed interface DeclarationMatcher<out D : Declaration> {
	
	companion object {
		
		val matchers = arrayOf(
			FunDeclarationMatcher,
			PropertyDeclarationMatcher,
			ClassDeclarationMatcher,
			ObjectDeclarationMatcher,
			ErrorDeclarationMatcher,
			TraitDeclarationMatcher,
			MixinDeclarationMatcher,
			StructDeclarationMatcher,
			EnumDeclarationMatcher,
			AnnotationDeclarationMatcher,
			ExtensionDeclarationMatcher,
			TypeAliasDeclarationMatcher
		)
	}
	
	val target: DeclarationTarget
	
	val modifierTarget: ModifierTarget
	
	context(cursor: PzlTokenCursor)
	fun match(): Boolean
	
	context(_: FileContext, cursor: PzlTokenCursor)
	fun parse(meta: DeclarationMeta, start: SourceLocation): D
}