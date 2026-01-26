package puzzle.frontend.parser.parser.statement

import puzzle.ast.parameter.ParameterReference
import puzzle.ast.statement.ForDestructurePattern
import puzzle.ast.statement.ForStatement
import puzzle.ast.statement.ForValuePattern
import puzzle.context.FileContext
import puzzle.base.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.IdentifierTarget
import puzzle.frontend.parser.parser.expression.parseExpressionChain
import puzzle.frontend.parser.parser.expression.parseIdentifier
import puzzle.frontend.parser.parser.expression.toIdentifier
import puzzle.frontend.parser.parser.type.parseTypeReference
import puzzle.token.kinds.BracketKind.End.RBRACKET
import puzzle.token.kinds.BracketKind.End.RPAREN
import puzzle.token.kinds.BracketKind.Start.*
import puzzle.token.kinds.OperatorKind.IN
import puzzle.token.kinds.SeparatorKind.COMMA
import puzzle.token.kinds.SymbolKind.AT
import puzzle.token.kinds.SymbolKind.COLON

context(_: FileContext, cursor: PzlTokenCursor)
fun parseForStatement(): ForStatement {
	val containsLabel = cursor.offset(-2).kind == AT
	val start = if (containsLabel) cursor.offset(-3).location else cursor.previous.location
	val label = if (containsLabel) cursor.offset(-3).toIdentifier() else null
	cursor.expect(LPAREN, "for 语句缺少 '('")
	val pattern = if (cursor.match(LBRACKET)) {
		val start = cursor.previous.location
		val references = buildList {
			while (!cursor.match(RBRACKET)) {
				val name = parseIdentifier(IdentifierTarget.FOR_PARAMETER_REFERENCE)
				val type = if (cursor.match(COLON)) {
					parseTypeReference(allowLambda = true)
				} else null
				this += ParameterReference(name, type)
				if (!cursor.check(RBRACKET)) {
					cursor.expect(COMMA, "for 语句解构参数列表缺少 ','")
				}
			}
		}
		val end = cursor.previous.location
		ForDestructurePattern(references, start span end)
	} else {
		val name = parseIdentifier(IdentifierTarget.FOR_PARAMETER_REFERENCE)
		val type = if (cursor.match(COLON)) parseTypeReference(allowLambda = true) else null
		val reference = ParameterReference(name, type)
		ForValuePattern(reference)
	}
	cursor.expect(IN, "for 语句缺少 in")
	val iterable = parseExpressionChain()
	cursor.expect(RPAREN, "for 语句缺少 ')'")
	val body = if (cursor.match(LBRACE)) {
		parseStatements()
	} else {
		listOf(parseStatement())
	}
	val end = cursor.previous.location
	return ForStatement(label, pattern, iterable, body, start span end)
}