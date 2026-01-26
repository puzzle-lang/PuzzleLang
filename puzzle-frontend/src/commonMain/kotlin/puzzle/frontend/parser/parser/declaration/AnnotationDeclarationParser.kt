package puzzle.frontend.parser.parser.declaration

import puzzle.ast.declaration.AnnotationDeclaration
import puzzle.context.FileContext
import puzzle.base.location.SourceLocation
import puzzle.base.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.frontend.parser.parser.expression.IdentifierTarget
import puzzle.frontend.parser.parser.expression.parseIdentifier
import puzzle.frontend.parser.parser.parameter.parameter.ParameterTarget
import puzzle.frontend.parser.parser.parameter.parameter.parseParameters
import puzzle.frontend.parser.syntaxError
import puzzle.token.kinds.BracketKind.Start.LBRACE
import puzzle.token.kinds.ContextualKind.WITH
import puzzle.token.kinds.SymbolKind.COLON

context(_: FileContext, cursor: PzlTokenCursor)
fun parseAnnotationDeclaration(meta: DeclarationMeta, start: SourceLocation): AnnotationDeclaration {
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
		docComment = meta.docComment,
		modifiers = meta.modifiers,
		parameters = parameters,
		typeSpec = meta.typeSpec,
		annotationCalls = meta.annotationCalls,
		location = start span end
	)
}