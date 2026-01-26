package puzzle.frontend.parser.parser.parameter.context

import puzzle.ast.parameter.DeclarationContextReceiver
import puzzle.ast.parameter.DeclarationContextSpec
import puzzle.context.FileContext
import puzzle.base.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.IdentifierTarget
import puzzle.frontend.parser.parser.expression.parseIdentifier
import puzzle.frontend.parser.parser.type.parseTypeReference
import puzzle.core.frontend.token.kinds.BracketKind.End.RPAREN
import puzzle.core.frontend.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.frontend.token.kinds.ContextualKind.CONTEXT
import puzzle.core.frontend.token.kinds.OperatorKind.NOT
import puzzle.core.frontend.token.kinds.SeparatorKind.COMMA
import puzzle.core.frontend.token.kinds.SymbolKind.COLON

context(_: FileContext, cursor: PzlTokenCursor)
fun parseDeclarationContextSpec(): DeclarationContextSpec? {
	if (!cursor.match(CONTEXT)) return null
	val start = cursor.previous.location
	cursor.expect(LPAREN, "context 缺少 '('")
	val receivers = buildList {
		do {
			this += parseDeclarationContextReceiver()
			if (!cursor.check(RPAREN)) {
				cursor.expect(COMMA, "context 参数列表缺少 ','")
			}
		} while (!cursor.match(RPAREN))
	}
	val isPropagate = !cursor.match(NOT)
	val end = cursor.previous.location
	return DeclarationContextSpec(receivers, isPropagate, start span end)
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseDeclarationContextReceiver(): DeclarationContextReceiver {
	val name = parseIdentifier(IdentifierTarget.CONTEXT_RECEIVER)
	cursor.expect(COLON, "context 参数缺少 ':'")
	val type = parseTypeReference()
	return DeclarationContextReceiver(name, type)
}