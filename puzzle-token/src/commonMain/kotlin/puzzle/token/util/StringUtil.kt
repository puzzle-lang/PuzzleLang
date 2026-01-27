package puzzle.token.util

import puzzle.core.util.isIdentifierPart
import puzzle.core.util.isIdentifierStart
import puzzle.token.kinds.KeywordKind

fun String.isIdentifierString(): Boolean {
	if (this.isEmpty()) return false
	val first = first()
	if (!first.isIdentifierStart()) return false
	if (this in KeywordKind.softKeywords) return true
	if (this in KeywordKind.hardKeywords) return false
	return this.drop(1).all { it.isIdentifierPart() }
}