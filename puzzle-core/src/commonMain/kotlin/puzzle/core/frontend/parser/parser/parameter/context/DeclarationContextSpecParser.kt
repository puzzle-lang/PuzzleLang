package puzzle.core.frontend.parser.parser.parameter.context

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.model.PzlContext
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.ast.parameter.DeclarationContextReceiver
import puzzle.core.frontend.ast.parameter.DeclarationContextSpec
import puzzle.core.frontend.parser.parser.expression.IdentifierTarget
import puzzle.core.frontend.parser.parser.expression.parseIdentifier
import puzzle.core.frontend.parser.parser.type.parseTypeReference
import puzzle.core.frontend.token.kinds.BracketKind.End.RPAREN
import puzzle.core.frontend.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.frontend.token.kinds.ContextualKind.CONTEXT
import puzzle.core.frontend.token.kinds.OperatorKind.NOT
import puzzle.core.frontend.token.kinds.SeparatorKind.COMMA
import puzzle.core.frontend.token.kinds.SymbolTokenKind.COLON

context(_: PzlContext, cursor: PzlTokenCursor)
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

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseDeclarationContextReceiver(): DeclarationContextReceiver {
	val name = parseIdentifier(IdentifierTarget.CONTEXT_RECEIVER)
	cursor.expect(COLON, "context 参数缺少 ':'")
	val type = parseTypeReference()
	return DeclarationContextReceiver(name, type)
}

context(_: PzlContext)
fun DeclarationContextSpec.check(target: ContextTarget) {
	if (!target.allowContext) {
		syntaxError("${target.label}不支持 context 上下文参数", this)
	}
}