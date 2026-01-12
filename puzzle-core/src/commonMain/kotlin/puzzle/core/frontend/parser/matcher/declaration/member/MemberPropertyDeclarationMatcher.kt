package puzzle.core.frontend.parser.matcher.declaration.member

import puzzle.core.frontend.ast.declaration.PropertyDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.matcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.matcher.declaration.DeclarationTarget
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parsePropertyDeclaration
import puzzle.core.frontend.token.kinds.ModifierKind.VAL
import puzzle.core.frontend.token.kinds.ModifierKind.VAR

object MemberPropertyDeclarationMatcher : MemberDeclarationMatcher<PropertyDeclaration> {
	
	override val target = DeclarationTarget.PROPERTY
	
	override val modifierTarget = ModifierTarget.MEMBER_PROPERTY
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		val kind = cursor.previous.kind
		return kind == VAR || kind == VAL
	}
	
	context(_: FileContext, cursor: PzlTokenCursor)
	override fun parse(meta: DeclarationMeta, start: SourceLocation): PropertyDeclaration {
		return parsePropertyDeclaration(meta, start, isTopLevel = false)
	}
}