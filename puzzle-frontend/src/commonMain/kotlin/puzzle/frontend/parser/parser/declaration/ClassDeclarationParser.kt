package puzzle.frontend.parser.parser.declaration

import puzzle.ast.declaration.ClassDeclaration
import puzzle.context.FileContext
import puzzle.base.location.SourceLocation
import puzzle.base.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMemberPolicy
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.frontend.parser.parser.ModifierTarget
import puzzle.frontend.parser.parser.check
import puzzle.frontend.parser.parser.expression.IdentifierTarget
import puzzle.frontend.parser.parser.expression.parseIdentifier
import puzzle.frontend.parser.parser.parameter.parameter.ParameterTarget
import puzzle.frontend.parser.parser.parameter.parameter.parseParameters
import puzzle.frontend.parser.parser.parseAnnotationCalls
import puzzle.frontend.parser.parser.parseModifiers
import puzzle.frontend.parser.parser.type.SuperTypeTarget
import puzzle.frontend.parser.parser.type.parseSuperTypes
import puzzle.frontend.parser.parser.type.parseWithTypes
import puzzle.token.kinds.BracketKind.Start.LBRACE

context(_: FileContext, cursor: PzlTokenCursor)
fun parseClassDeclaration(meta: DeclarationMeta, start: SourceLocation): ClassDeclaration {
	val name = parseIdentifier(IdentifierTarget.CLASS)
	val primaryCtorAnnotationCalls = parseAnnotationCalls()
	val primaryCtorModifiers = parseModifiers()
	primaryCtorModifiers.check(ModifierTarget.CTOR)
	val parameters = parseParameters(ParameterTarget.CLASS)
	val superTypes = parseSuperTypes(SuperTypeTarget.CLASS)
	val withTypes = parseWithTypes()
	val info = if (cursor.match(LBRACE)) {
		parseMemberDeclarationInfo(DeclarationMemberPolicy.CLASS)
	} else MemberDeclarationInfo.Empty
	val end = cursor.previous.location
	return ClassDeclaration(
		name = name,
		docComment = meta.docComment,
		modifiers = meta.modifiers,
		primaryCtorAnnotationCalls = primaryCtorAnnotationCalls,
		primaryCtorModifiers = primaryCtorModifiers,
		parameters = parameters,
		typeSpec = meta.typeSpec,
		contextSpec = meta.contextSpec,
		annotationCalls = meta.annotationCalls,
		superTypes = superTypes,
		withTypes = withTypes,
		ctors = info.ctors,
		inits = info.inits,
		members = info.members,
		location = start span end
	)
}