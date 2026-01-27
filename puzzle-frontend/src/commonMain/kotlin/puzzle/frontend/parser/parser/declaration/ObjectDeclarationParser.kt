package puzzle.frontend.parser.parser.declaration

import puzzle.ast.declaration.ObjectDeclaration
import puzzle.core.context.FileContext
import puzzle.core.location.SourceLocation
import puzzle.core.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMemberPolicy
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.frontend.parser.parser.ModifierTarget
import puzzle.frontend.parser.parser.check
import puzzle.frontend.parser.parser.expression.IdentifierTarget
import puzzle.frontend.parser.parser.expression.parseIdentifier
import puzzle.frontend.parser.parser.expression.tryParseIdentifier
import puzzle.frontend.parser.parser.parameter.parameter.ParameterTarget
import puzzle.frontend.parser.parser.parameter.parameter.parseParameters
import puzzle.frontend.parser.parser.parseAnnotationCalls
import puzzle.frontend.parser.parser.parseModifiers
import puzzle.frontend.parser.parser.type.SuperTypeTarget
import puzzle.frontend.parser.parser.type.parseSuperTypes
import puzzle.frontend.parser.parser.type.parseWithTypes
import puzzle.token.kinds.BracketKind.Start.LBRACE

context(_: FileContext, cursor: PzlTokenCursor)
fun parseObjectDeclaration(meta: DeclarationMeta, start: SourceLocation, isTopLevel: Boolean): ObjectDeclaration {
	val name = if (isTopLevel) {
		parseIdentifier(IdentifierTarget.OBJECT)
	} else {
		tryParseIdentifier(IdentifierTarget.OBJECT)
	}
	val primaryCtorAnnotationCalls = parseAnnotationCalls()
	val primaryCtorModifiers = parseModifiers()
	primaryCtorModifiers.check(ModifierTarget.CTOR)
	val parameters = parseParameters(ParameterTarget.OBJECT)
	val superTypes = parseSuperTypes(SuperTypeTarget.OBJECT)
	val withTypes = parseWithTypes()
	val info = if (cursor.match(LBRACE)) {
		parseMemberDeclarationInfo(DeclarationMemberPolicy.OBJECT)
	} else MemberDeclarationInfo.Empty
	val end = cursor.previous.location
	return ObjectDeclaration(
		name = name,
		docComment = meta.docComment,
		modifiers = meta.modifiers,
		primaryCtorAnnotationCalls = primaryCtorAnnotationCalls,
		primaryCtorModifiers = primaryCtorModifiers,
		parameters = parameters,
		superTypes = superTypes,
		withTypes = withTypes,
		contextSpec = meta.contextSpec,
		annotationCalls = meta.annotationCalls,
		inits = info.inits,
		ctors = info.ctors,
		members = info.members,
		location = start span end
	)
}