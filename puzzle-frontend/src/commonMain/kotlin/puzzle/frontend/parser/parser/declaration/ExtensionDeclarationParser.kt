package puzzle.frontend.parser.parser.declaration

import puzzle.ast.declaration.ExtensionDeclaration
import puzzle.ast.type.SuperTypeReference
import puzzle.context.FileContext
import puzzle.base.location.SourceLocation
import puzzle.base.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMemberPolicy
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.frontend.parser.parser.expression.IdentifierTarget
import puzzle.frontend.parser.parser.expression.parseIdentifier
import puzzle.frontend.parser.parser.type.SuperTypeTarget
import puzzle.frontend.parser.parser.type.parseSuperTypes
import puzzle.frontend.parser.parser.type.parseTypeReference
import puzzle.frontend.parser.parser.type.parseWithTypes
import puzzle.token.kinds.BracketKind.Start.LBRACE
import puzzle.token.kinds.TypeOperatorKind.AS

context(_: FileContext, cursor: PzlTokenCursor)
fun parseExtensionDeclaration(meta: DeclarationMeta, start: SourceLocation): ExtensionDeclaration {
	val extendedType = parseTypeReference()
	val superTypes = parseSuperTypes(SuperTypeTarget.EXTENSION)
		.filterIsInstance<SuperTypeReference>()
	val withTypes = parseWithTypes()
	cursor.expect(AS, "扩展缺少别名")
	val alias = parseIdentifier(IdentifierTarget.EXTENSION_AS)
	val info = if (cursor.match(LBRACE)) {
		parseMemberDeclarationInfo(DeclarationMemberPolicy.EXTENSION)
	} else MemberDeclarationInfo.Empty
	val end = cursor.previous.location
	return ExtensionDeclaration(
		alias = alias,
		extendedType = extendedType,
		modifiers = meta.modifiers,
		superTypes = superTypes,
		withTypes = withTypes,
		typeSpec = meta.typeSpec,
		contextSpec = meta.contextSpec,
		annotationCalls = meta.annotationCalls,
		members = info.members,
		location = start span end
	)
}