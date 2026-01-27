package puzzle.frontend.util

import puzzle.core.location.SourceLocation
import puzzle.core.context.FileContext
import puzzle.core.context.endPosition
import puzzle.core.context.startPosition
import puzzle.token.PzlToken

context(_: FileContext)
fun PzlToken.equalsLine(token: PzlToken): Boolean {
	val left = this.location
	val right = token.location
	if (left !is SourceLocation.File || right !is SourceLocation.File) {
		return false
	}
	return left.endPosition.line == right.startPosition.line
}