package puzzle.frontend.util

import puzzle.token.kinds.KeywordKind

fun Char.isIdentifierStart(): Boolean {
	return this in 'a'..'z' || this in 'A'..'Z' || this == '_'
}

fun Char.isIdentifierPart(): Boolean {
	return this in 'a'..'z' || this in 'A'..'Z' || this == '_' || this in '0'..'9'
}

fun String.isIdentifierString(): Boolean {
	if (this.isEmpty()) return false
	val first = first()
	if (!first.isIdentifierStart()) return false
	if (this in KeywordKind.softKeywords) return true
	if (this in KeywordKind.hardKeywords) return false
	return this.drop(1).all { it.isIdentifierPart() }
}