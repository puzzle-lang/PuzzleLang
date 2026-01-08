package puzzle.core.frontend.parser.parser.expression

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.expression.Argument
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.token.kinds.AssignmentKind.ASSIGN
import puzzle.core.frontend.token.kinds.BracketKind
import puzzle.core.frontend.token.kinds.BracketKind.End.RBRACKET
import puzzle.core.frontend.token.kinds.BracketKind.End.RPAREN
import puzzle.core.frontend.token.kinds.BracketKind.Start.LBRACKET
import puzzle.core.frontend.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.frontend.token.kinds.SeparatorKind.COMMA
import puzzle.core.frontend.token.kinds.SeparatorKind.SEMICOLON

context(_: FileContext, cursor: PzlTokenCursor)
fun parseArguments(target: ArgumentTarget): List<Argument> {
	if (cursor.match(target.end)) return emptyList()
	return buildList {
		do {
			this += parseCallArgument(target.end)
			if (!cursor.check(target.end)) {
				cursor.expect(COMMA, "实参列表缺少 ','")
			}
		} while (!cursor.match(target.end))
		if (target.allowTrailingClosure && cursor.matchLambda()) {
			val expression = parseLambdaExpression()
			this += Argument(null, expression)
		}
	}
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun parseCallArgument(endKind: BracketKind.End): Argument {
	val name = if (cursor.offsetOrNull(offset = 1)?.kind == ASSIGN) {
		parseIdentifier(IdentifierTarget.ARGUMENT).also {
			cursor.advance()
		}
	} else null
	val expression = parseExpressionChain()
	if (cursor.previous.kind == SEMICOLON) {
		syntaxError("参数不支持使用 ';' 结束表达式", cursor.previous)
	}
	val end = cursor.previous.location
	val currentKind = cursor.current.kind
	if (currentKind == COMMA) {
		return Argument(name, expression)
	}
	when (endKind) {
		LPAREN if currentKind == RBRACKET -> syntaxError(
			message = "参数表达式后只允许根 ')' 或 ','",
			token = cursor.current
		)
		
		LBRACKET if currentKind == RPAREN -> syntaxError(
			message = "索引访问参数表达式后只允许根 ']' 或 ','",
			token = cursor.current
		)
		
		else -> return Argument(name, expression)
	}
}

enum class ArgumentTarget(
	val end: BracketKind.End,
	val allowTrailingClosure: Boolean,
) {
	ANNOTATION(
		end = RPAREN,
		allowTrailingClosure = false
	),
	CALL(
		end = RPAREN,
		allowTrailingClosure = true
	),
	INDEX_ACCESS(
		end = RBRACKET,
		allowTrailingClosure = false
	),
	CONTEXTUAL(
		end = RPAREN,
		allowTrailingClosure = false
	),
	SUPER_CONSTRUCTOR_CALL(
		end = RPAREN,
		allowTrailingClosure = false
	)
}