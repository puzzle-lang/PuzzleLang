package puzzle.core.frontend.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.declaration.CtorDeclaration
import puzzle.core.frontend.ast.statement.Statement
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.core.frontend.parser.dispatcher.statement.ContextualStatementDispatcher
import puzzle.core.frontend.parser.parser.expression.IdentifierTarget
import puzzle.core.frontend.parser.parser.expression.tryParseIdentifier
import puzzle.core.frontend.parser.parser.parameter.parameter.ParameterTarget
import puzzle.core.frontend.parser.parser.parameter.parameter.parseParameters
import puzzle.core.frontend.parser.parser.statement.parseStatements
import puzzle.core.frontend.token.kinds.BracketKind
import puzzle.core.frontend.token.kinds.BracketKind.Start.LBRACE
import puzzle.core.frontend.token.kinds.ContextualKind.SUPER
import puzzle.core.frontend.token.kinds.ContextualKind.THIS
import puzzle.core.frontend.token.kinds.SymbolKind.COLON

context(_: FileContext, cursor: PzlTokenCursor)
fun parseCtorDeclaration(meta: DeclarationMeta, start: SourceLocation): CtorDeclaration {
	val name = tryParseIdentifier(IdentifierTarget.CTOR)
	val parameters = parseParameters(ParameterTarget.CTOR)
	val body = buildList<Statement> {
		if (cursor.match(COLON)) {
			if (cursor.match { it.kind == THIS || it.kind == SUPER }) {
				val type = if (cursor.previous.kind == THIS) "this" else "super"
				cursor.expect(BracketKind.Start.LPAREN, "$type 后缺少 '('")
				ContextualStatementDispatcher.parse()
			} else syntaxError("次构造函数的 ':' 后只允许跟 this 或 super", cursor.current)
		}
		if (cursor.match(LBRACE)) {
			this += parseStatements()
		}
	}
	val end = cursor.previous.location
	return CtorDeclaration(
		name = name,
		docComment = meta.docComment,
		parameters = parameters,
		modifiers = meta.modifiers,
		annotationCalls = meta.annotationCalls,
		body = body,
		location = start span end
	)
}