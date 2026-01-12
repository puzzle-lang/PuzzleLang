package puzzle.core.frontend.parser.matcher.declaration.member

import puzzle.core.frontend.ast.declaration.Declaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.matcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.matcher.declaration.DeclarationTarget
import puzzle.core.frontend.parser.parser.ModifierTarget

sealed interface MemberDeclarationMatcher<out D : Declaration> {
	
	companion object {
		
		val matchers = arrayOf(
			MemberFunDeclarationMatcher,
			MemberPropertyDeclarationMatcher,
			MemberClassDeclarationMatcher,
			MemberObjectDeclarationMatcher,
			MemberErrorDeclarationMatcher,
			MemberTraitDeclarationMatcher,
			MemberMixinDeclarationMatcher,
			MemberStructDeclarationMatcher,
			MemberEnumDeclarationMatcher,
			MemberAnnotationDeclarationMatcher,
			MemberExtensionDeclarationMatcher,
			MemberTypeAliasDeclarationMatcher,
			MemberCtorDeclarationMatcher,
			MemberInitDeclarationMatcher
		)
	}
	
	val target: DeclarationTarget
	
	val modifierTarget: ModifierTarget
	
	context(cursor: PzlTokenCursor)
	fun match(): Boolean
	
	context(_: FileContext, cursor: PzlTokenCursor)
	fun parse(meta: DeclarationMeta, start: SourceLocation): D
}