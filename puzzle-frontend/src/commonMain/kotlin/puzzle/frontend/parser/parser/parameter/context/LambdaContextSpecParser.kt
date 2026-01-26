package puzzle.frontend.parser.parser.parameter.context

import puzzle.ast.parameter.LambdaContextReceiver
import puzzle.ast.parameter.LambdaContextSpec
import puzzle.context.FileContext
import puzzle.base.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.checkIdentifier
import puzzle.frontend.parser.parser.parameter.parseTypeExpansion
import puzzle.frontend.parser.parser.type.parseTypeReference
import puzzle.frontend.parser.syntaxError
import puzzle.token.kinds.BracketKind.End.RPAREN
import puzzle.token.kinds.BracketKind.Start.LPAREN
import puzzle.token.kinds.ContextualKind.CONTEXT
import puzzle.token.kinds.OperatorKind.NOT
import puzzle.token.kinds.SeparatorKind.COMMA
import puzzle.token.kinds.SymbolKind.COLON

context(_: FileContext, cursor: PzlTokenCursor)
fun parseLambdaContextSpec(): LambdaContextSpec? {
	if (!cursor.check(CONTEXT) || cursor.nextOrNull?.kind != LPAREN) return null
	val start = cursor.current.location
	cursor.advance(2)
	val receivers = buildList {
		do {
			if (cursor.checkIdentifier(allowAnonymousBinding = true) && cursor.nextOrNull?.kind == COLON) {
				syntaxError("lambda 上下文参数不支持命名", cursor.current)
			}
			this += parseLambdaContextReceiver()
			if (!cursor.check(RPAREN)) {
				cursor.expect(COMMA, "context 参数列表缺少 ','")
			}
		} while (!cursor.match(RPAREN))
	}
	val isPropagate = !cursor.match(NOT)
	val end = cursor.previous.location
	return LambdaContextSpec(receivers, isPropagate, start span end)
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseLambdaContextReceiver(): LambdaContextReceiver {
	val type = parseTypeReference(allowLambda = true)
	val typeExpansion = parseTypeExpansion()
	val end = cursor.previous.location
	return LambdaContextReceiver(type, typeExpansion, type.location span end)
}