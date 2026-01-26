package puzzle.frontend.parser.parser.declaration

import puzzle.ast.declaration.CtorDeclaration
import puzzle.ast.statement.Statement
import puzzle.context.FileContext
import puzzle.base.location.SourceLocation
import puzzle.base.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.dispatcher.declaration.DeclarationMeta
import puzzle.frontend.parser.dispatcher.statement.ContextualStatementDispatcher
import puzzle.frontend.parser.parser.expression.IdentifierTarget
import puzzle.frontend.parser.parser.expression.tryParseIdentifier
import puzzle.frontend.parser.parser.parameter.parameter.ParameterTarget
import puzzle.frontend.parser.parser.parameter.parameter.parseParameters
import puzzle.frontend.parser.parser.statement.parseStatements
import puzzle.frontend.parser.syntaxError
import puzzle.token.kinds.BracketKind.Start.LBRACE
import puzzle.token.kinds.BracketKind.Start.LPAREN
import puzzle.token.kinds.ContextualKind.SUPER
import puzzle.token.kinds.ContextualKind.THIS
import puzzle.token.kinds.SymbolKind

context(_: FileContext, cursor: PzlTokenCursor)
fun parseCtorDeclaration(meta: DeclarationMeta, start: SourceLocation): CtorDeclaration {
	val name = tryParseIdentifier(IdentifierTarget.CTOR)
	val parameters = parseParameters(ParameterTarget.CTOR)
	val body = buildList<Statement> {
		if (cursor.match(SymbolKind.COLON)) {
			if (cursor.match { it.kind == THIS || it.kind == SUPER }) {
				val type = cursor.previous.value
				cursor.expect(LPAREN, "$type 后缺少 '('")
				ContextualStatementDispatcher.parse()
			} else {
				syntaxError("次构造函数的 ':' 后只允许跟 this 或 super", cursor.current)
			}
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