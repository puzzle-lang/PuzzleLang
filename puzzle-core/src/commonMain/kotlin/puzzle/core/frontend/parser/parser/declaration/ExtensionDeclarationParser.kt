package puzzle.core.frontend.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.declaration.ExtensionDeclaration
import puzzle.core.frontend.ast.type.SuperTypeReference
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.matcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.parser.type.SuperTypeTarget
import puzzle.core.frontend.parser.parser.type.parseSuperTypes
import puzzle.core.frontend.parser.parser.type.parseTypeReference
import puzzle.core.frontend.parser.parser.type.parseWithTypes
import puzzle.core.frontend.token.kinds.BracketKind.Start.LBRACE

context(_: FileContext, cursor: PzlTokenCursor)
fun parseExtensionDeclaration(meta: DeclarationMeta, start: SourceLocation): ExtensionDeclaration {
	val extendedType = parseTypeReference()
	val superTypes = parseSuperTypes(SuperTypeTarget.EXTENSION)
		.filterIsInstance<SuperTypeReference>()
	val withTypes = parseWithTypes()
	val info = if (cursor.match(LBRACE)) {
		parseMemberDeclarationInfo()
	} else MemberDeclarationInfo.Empty
	if (info.inits.isNotEmpty()) {
		syntaxError("扩展不允许有初始化块", info.inits.first())
	}
	if (info.ctors.isNotEmpty()) {
		syntaxError("扩展不允许有次构造函数", info.ctors.first())
	}
	val end = cursor.previous.location
	return ExtensionDeclaration(
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