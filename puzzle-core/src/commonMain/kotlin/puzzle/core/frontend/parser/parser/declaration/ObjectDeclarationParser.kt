package puzzle.core.frontend.parser.parser.declaration

import puzzle.core.frontend.ast.declaration.ObjectDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationMemberPolicy
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.check
import puzzle.core.frontend.parser.parser.expression.IdentifierTarget
import puzzle.core.frontend.parser.parser.expression.parseIdentifier
import puzzle.core.frontend.parser.parser.expression.tryParseIdentifier
import puzzle.core.frontend.parser.parser.parameter.parameter.ParameterTarget
import puzzle.core.frontend.parser.parser.parameter.parameter.parseParameters
import puzzle.core.frontend.parser.parser.parseAnnotationCalls
import puzzle.core.frontend.parser.parser.parseModifiers
import puzzle.core.frontend.parser.parser.type.SuperTypeTarget
import puzzle.core.frontend.parser.parser.type.parseSuperTypes
import puzzle.core.frontend.parser.parser.type.parseWithTypes
import puzzle.core.frontend.token.kinds.BracketKind.Start.LBRACE

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