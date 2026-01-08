package puzzle.core.frontend.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.ast.declaration.AnnotationDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.matcher.declaration.DeclarationHeader
import puzzle.core.frontend.parser.parser.expression.IdentifierTarget
import puzzle.core.frontend.parser.parser.expression.parseIdentifier
import puzzle.core.frontend.parser.parser.parameter.parameter.ParameterTarget
import puzzle.core.frontend.parser.parser.parameter.parameter.parseParameters
import puzzle.core.frontend.token.kinds.BracketKind.Start.LBRACE
import puzzle.core.frontend.token.kinds.ContextualKind.WITH
import puzzle.core.frontend.token.kinds.SymbolTokenKind.COLON

context(_: FileContext, cursor: PzlTokenCursor)
fun parseAnnotationDeclaration(header: DeclarationHeader, start: SourceLocation): AnnotationDeclaration {
	val name = parseIdentifier(IdentifierTarget.ANNOTATION)
	val parameters = parseParameters(ParameterTarget.ANNOTATION)
	if (cursor.match(COLON)) {
		syntaxError("注解不支持 ':'", cursor.previous)
	}
	if (cursor.match(WITH)) {
		syntaxError("注解不支持 with", cursor.previous)
	}
	if (cursor.match(LBRACE)) {
		syntaxError("注解不支持 '{'", cursor.previous)
	}
	val end = cursor.previous.location
	return AnnotationDeclaration(
		name = name,
		docComment = header.docComment,
		modifiers = header.modifiers,
		parameters = parameters,
		typeSpec = header.typeSpec,
		annotationCalls = header.annotationCalls,
		location = start span end
	)
}