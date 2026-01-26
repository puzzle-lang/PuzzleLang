package puzzle.frontend.util

import puzzle.base.location.SourceLocation
import puzzle.context.FileContext
import puzzle.context.endPosition
import puzzle.context.startPosition
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