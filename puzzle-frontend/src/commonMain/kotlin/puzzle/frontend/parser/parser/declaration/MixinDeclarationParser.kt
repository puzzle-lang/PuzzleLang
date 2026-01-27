package puzzle.frontend.parser.parser.declaration

import puzzle.ast.declaration.MixinDeclaration
import puzzle.ast.type.NamedType
import puzzle.core.context.FileContext
import puzzle.core.location.SourceLocation
import puzzle.core.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMemberPolicy
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.frontend.parser.parser.expression.IdentifierTarget
import puzzle.frontend.parser.parser.expression.parseIdentifier
import puzzle.frontend.parser.parser.type.parseNamedType
import puzzle.frontend.parser.parser.type.parseWithTypes
import puzzle.token.kinds.BracketKind.Start.LBRACE
import puzzle.token.kinds.ContextualKind.ON
import puzzle.token.kinds.SeparatorKind.COMMA

context(_: FileContext, cursor: PzlTokenCursor)
fun parseMixinDeclaration(meta: DeclarationMeta, start: SourceLocation): MixinDeclaration {
	val name = parseIdentifier(IdentifierTarget.MIXIN)
	val mixinConstraints = parseMixinConstraints()
	val withTypes = parseWithTypes()
	val info = if (cursor.match(LBRACE)) {
		parseMemberDeclarationInfo(DeclarationMemberPolicy.MIXIN)
	} else MemberDeclarationInfo.Empty
	val end = cursor.previous.location
	return MixinDeclaration(
		name = name,
		docComment = meta.docComment,
		modifiers = meta.modifiers,
		typeSpec = meta.typeSpec,
		contextSpec = meta.contextSpec,
		annotationCalls = meta.annotationCalls,
		mixinConstraints = mixinConstraints,
		withTypes = withTypes,
		members = info.members,
		location = start span end
	)
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseMixinConstraints(): List<NamedType> {
	if (!cursor.match(ON)) return emptyList()
	return buildList {
		do {
			this += parseNamedType()
		} while (cursor.match(COMMA))
	}
}