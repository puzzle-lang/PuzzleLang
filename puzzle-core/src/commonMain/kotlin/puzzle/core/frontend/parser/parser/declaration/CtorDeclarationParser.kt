package puzzle.core.frontend.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.model.PzlContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.ast.declaration.CtorDeclaration
import puzzle.core.frontend.ast.statement.Statement
import puzzle.core.frontend.parser.matcher.declaration.DeclarationHeader
import puzzle.core.frontend.parser.matcher.statement.ContextualStatementMatcher
import puzzle.core.frontend.parser.parser.expression.IdentifierTarget
import puzzle.core.frontend.parser.parser.expression.tryParseIdentifier
import puzzle.core.frontend.parser.parser.parameter.parameter.ParameterTarget
import puzzle.core.frontend.parser.parser.parameter.parameter.parseParameters
import puzzle.core.frontend.parser.parser.statement.parseStatements
import puzzle.core.frontend.token.kinds.BracketKind.Start.LBRACE
import puzzle.core.frontend.token.kinds.SymbolTokenKind.COLON

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseCtorDeclaration(
	header: DeclarationHeader,
	start: SourceLocation,
): CtorDeclaration {
	val name = tryParseIdentifier(IdentifierTarget.CTOR)
	val parameters = parseParameters(ParameterTarget.CTOR)
	val body = buildList<Statement> {
		if (cursor.match(COLON)) {
			this += with(ContextualStatementMatcher) {
				if (match()) parse() else syntaxError("次构造函数的 ':' 后只允许跟 this 或 super", cursor.current)
			}
		}
		if (cursor.match(LBRACE)) {
			this += parseStatements()
		}
	}
	val end = cursor.previous.location
	return CtorDeclaration(
		name = name,
		docComment = header.docComment,
		parameters = parameters,
		modifiers = header.modifiers,
		contextSpec = header.contextSpec,
		annotationCalls = header.annotationCalls,
		body = body,
		location = start span end
	)
}