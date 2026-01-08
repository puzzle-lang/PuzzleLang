package puzzle.core.frontend.parser.parser.statement

import puzzle.core.frontend.ast.statement.ContextualStatement
import puzzle.core.frontend.ast.statement.SuperStatement
import puzzle.core.frontend.ast.statement.ThisStatement
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.expression.ArgumentTarget
import puzzle.core.frontend.parser.parser.expression.parseArguments
import puzzle.core.frontend.token.kinds.ContextualKind.THIS

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