package puzzle.core.frontend.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.declaration.StructDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.parser.expression.IdentifierTarget
import puzzle.core.frontend.parser.parser.expression.parseIdentifier
import puzzle.core.frontend.parser.parser.parameter.parameter.ParameterTarget
import puzzle.core.frontend.parser.parser.parameter.parameter.parseParameters
import puzzle.core.frontend.parser.parser.parseAnnotationCalls
import puzzle.core.frontend.parser.parser.parseModifiers
import puzzle.core.frontend.parser.parser.type.SuperTypeTarget
import puzzle.core.frontend.parser.parser.type.parseSuperTypes
import puzzle.core.frontend.parser.parser.type.safeAsSuperTypeReferences
import puzzle.core.frontend.token.kinds.BracketKind.Start.LBRACE
import puzzle.core.frontend.token.kinds.ContextualKind.WITH

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
		parseMemberDeclarationInfo()
	} else MemberDeclarationInfo.Empty
	if (info.ctors.isNotEmpty()) {
		syntaxError("结构体不允许有次构造函数", info.ctors.first())
	}
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