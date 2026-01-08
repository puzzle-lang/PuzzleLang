package puzzle.core.frontend.parser.matcher.declaration.member

import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.ast.declaration.ClassDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.matcher.declaration.DeclarationHeader
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parseClassDeclaration
import puzzle.core.frontend.parser.parser.parameter.context.ContextTarget
import puzzle.core.frontend.parser.parser.parameter.type.TypeTarget
import puzzle.core.frontend.token.kinds.DeclarationKind.CLASS

object MemberClassDeclarationMatcher : MemberDeclarationMatcher<ClassDeclaration> {
	
	override val typeTarget = TypeTarget.CLASS
	
	override val modifierTarget = ModifierTarget.MEMBER_CLASS
	
	override val contextTarget = ContextTarget.CLASS
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(CLASS)
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(header: DeclarationHeader, start: SourceLocation): ClassDeclaration {
		return parseClassDeclaration(header, start)
	}
}