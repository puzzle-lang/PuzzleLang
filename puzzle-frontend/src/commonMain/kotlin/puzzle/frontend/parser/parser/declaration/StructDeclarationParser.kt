package puzzle.frontend.parser.parser.declaration

import puzzle.ast.declaration.StructDeclaration
import puzzle.core.context.FileContext
import puzzle.core.location.SourceLocation
import puzzle.core.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMemberPolicy
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.frontend.parser.parser.expression.IdentifierTarget
import puzzle.frontend.parser.parser.expression.parseIdentifier
import puzzle.frontend.parser.parser.parameter.parameter.ParameterTarget
import puzzle.frontend.parser.parser.parameter.parameter.parseParameters
import puzzle.frontend.parser.parser.parseAnnotationCalls
import puzzle.frontend.parser.parser.parseModifiers
import puzzle.frontend.parser.parser.type.SuperTypeTarget
import puzzle.frontend.parser.parser.type.parseSuperTypes
import puzzle.frontend.parser.parser.type.safeAsSuperTypeReferences
import puzzle.frontend.parser.syntaxError
import puzzle.token.kinds.BracketKind.Start.LBRACE
import puzzle.token.kinds.ContextualKind.WITH

context(_: FileContext, cursor: PzlTokenCursor)
fun parseStructDeclaration(meta: DeclarationMeta, start: SourceLocation): StructDeclaration {
	val name = parseIdentifier(IdentifierTarget.STRUCT)
	val primaryCtorAnnotationCalls = parseAnnotationCalls()
	val primaryCtorModifiers = parseModifiers()
	val parameters = parseParameters(ParameterTarget.STRUCT)
	val superTypes = parseSuperTypes(SuperTypeTarget.STRUCT)
		.safeAsSuperTypeReferences()
	if (cursor.match(WITH)) {
		syntaxError("结构体不支持 with", cursor.previous)
	}
	val info = if (cursor.match(LBRACE)) {
		parseMemberDeclarationInfo(DeclarationMemberPolicy.STRUCT)
	} else MemberDeclarationInfo.Empty
	val end = cursor.previous.location
	return StructDeclaration(
		name = name,
		docComment = meta.docComment,
		modifiers = meta.modifiers,
		primaryCtorAnnotationCalls = primaryCtorAnnotationCalls,
		primaryCtorModifiers = primaryCtorModifiers,
		parameters = parameters,
		superTypes = superTypes,
		typeSpec = meta.typeSpec,
		contextSpec = meta.contextSpec,
		annotationCalls = meta.annotationCalls,
		inits = info.inits,
		members = info.members,
		location = start span end
	)
}