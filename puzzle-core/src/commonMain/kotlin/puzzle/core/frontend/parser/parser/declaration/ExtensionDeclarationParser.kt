package puzzle.core.frontend.parser.parser.declaration

import puzzle.core.frontend.ast.declaration.ExtensionDeclaration
import puzzle.core.frontend.ast.type.SuperTypeReference
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationMemberPolicy
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.parser.expression.IdentifierTarget
import puzzle.core.frontend.parser.parser.expression.parseIdentifier
import puzzle.core.frontend.parser.parser.type.SuperTypeTarget
import puzzle.core.frontend.parser.parser.type.parseSuperTypes
import puzzle.core.frontend.parser.parser.type.parseTypeReference
import puzzle.core.frontend.parser.parser.type.parseWithTypes
import puzzle.core.frontend.token.kinds.BracketKind.Start.LBRACE
import puzzle.core.frontend.token.kinds.TypeOperatorKind.AS

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