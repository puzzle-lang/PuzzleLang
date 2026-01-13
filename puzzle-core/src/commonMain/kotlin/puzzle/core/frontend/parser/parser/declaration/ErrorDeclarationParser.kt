package puzzle.core.frontend.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.declaration.ErrorDeclaration
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.parser.expression.IdentifierTarget
import puzzle.core.frontend.parser.parser.expression.parseIdentifier
import puzzle.core.frontend.parser.parser.parameter.parameter.ParameterTarget
import puzzle.core.frontend.parser.parser.parameter.parameter.parseParameters
import puzzle.core.frontend.token.kinds.BracketKind.Start.LBRACE
import puzzle.core.frontend.token.kinds.ContextualKind.WITH
import puzzle.core.frontend.token.kinds.SymbolKind.COLON

context(_: FileContext, cursor: PzlTokenCursor)
fun parseErrorDeclaration(meta: DeclarationMeta, start: SourceLocation): ErrorDeclaration {
	val name = parseIdentifier(IdentifierTarget.ERROR)
	val parameters = parseParameters(ParameterTarget.ERROR)
	if (cursor.match(COLON)) {
		syntaxError("错误不支持 ':'", cursor.previous)
	}
	if (cursor.match(WITH)) {
		syntaxError("错误不支持 with", cursor.previous)
	}
	if (cursor.match(LBRACE)) {
		syntaxError("错误不支持 '{'", cursor.previous)
	}
	val end = cursor.previous.location
	return ErrorDeclaration(
		name = name,
		docComment = meta.docComment,
		modifiers = meta.modifiers,
		parameters = parameters,
		typeSpec = meta.typeSpec,
		annotationCalls = meta.annotationCalls,
		location = start span end
	)
}