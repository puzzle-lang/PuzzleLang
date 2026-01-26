package puzzle.frontend.parser.parser.statement

import puzzle.ast.statement.ContextualStatement
import puzzle.ast.statement.SuperStatement
import puzzle.ast.statement.ThisStatement
import puzzle.context.FileContext
import puzzle.base.location.span
import puzzle.frontend.parser.PzlTokenCursor
import puzzle.frontend.parser.parser.expression.ArgumentTarget
import puzzle.frontend.parser.parser.expression.parseArguments
import puzzle.token.kinds.ContextualKind.THIS

context(_: FileContext, cursor: PzlTokenCursor)
fun parseContextualStatement(): ContextualStatement {
	val token = cursor.offset(-2)
	val start = token.location
	val arguments = parseArguments(ArgumentTarget.CONTEXTUAL)
	val end = cursor.previous.location
	return if (token.kind == THIS) {
		ThisStatement(arguments, start span end)
	} else {
		SuperStatement(arguments, end span start)
	}
}