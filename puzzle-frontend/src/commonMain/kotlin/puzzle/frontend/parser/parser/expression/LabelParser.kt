package puzzle.frontend.parser.parser.expression

import puzzle.frontend.parser.PzlTokenCursor
import puzzle.token.PzlToken
import puzzle.token.kinds.PzlTokenKind
import puzzle.token.kinds.SymbolKind.AT

fun PzlTokenCursor.matchLabel(kind: PzlTokenKind): Boolean {
	if (!checkIdentifier()) return false
	if (nextOrNull?.kind != AT) return false
	if (offsetOrNull(2)?.kind != kind) return false
	this.advance(3)
	return true
}

fun PzlTokenCursor.matchLabel(predicate: (PzlToken) -> Boolean): Boolean {
	if (!checkIdentifier()) return false
	if (nextOrNull?.kind != AT) return false
	val token = offsetOrNull(2) ?: return false
	if (!predicate(token)) return false
	this.advance(3)
	return true
}