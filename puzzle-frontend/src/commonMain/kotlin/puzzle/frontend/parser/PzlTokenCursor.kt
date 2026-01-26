package puzzle.frontend.parser

import puzzle.context.FileContext
import puzzle.token.PzlToken
import puzzle.token.kinds.MetaKind.EOF
import puzzle.token.kinds.PzlTokenKind

class PzlTokenCursor(
	private val tokens: List<PzlToken>,
) {
	
	var position = 0
		private set
	
	val current: PzlToken
		get() = this.tokens[position]
	
	val previous: PzlToken
		get() = this.tokens[position - 1]
	
	val previousOrNull: PzlToken?
		get() = this.tokens.getOrNull(position - 1)
	
	val next: PzlToken
		get() = this.tokens[position + 1]
	
	val nextOrNull: PzlToken?
		get() = this.tokens.getOrNull(position + 1)
	
	fun offset(offset: Int): PzlToken {
		return tokens[position + offset]
	}
	
	fun offsetOrNull(offset: Int): PzlToken? {
		return tokens.getOrNull(position + offset)
	}
	
	fun advance(count: Int = 1) {
		require(count >= 0) { "count 必须大于等于 0" }
		if (position <= tokens.size - count) {
			position += count
		} else {
			error("position: $position, tokenSize: ${tokens.size}")
		}
	}
	
	fun retreat(count: Int = 1) {
		require(count >= 0) { "count 必须大于等于 0" }
		if (position >= count) {
			position -= count
		} else {
			error("position: $position, tokenSize: ${tokens.size}")
		}
	}
	
	fun match(kind: PzlTokenKind): Boolean {
		if (current.kind != kind) return false
		position++
		return true
	}
	
	fun match(kind1: PzlTokenKind, kind2: PzlTokenKind, vararg kinds: PzlTokenKind): Boolean {
		val kinds = arrayOf(kind1, kind2, *kinds)
		kinds.forEachIndexed { offset, kind ->
			val token = offsetOrNull(offset = offset) ?: return false
			if (token.kind != kind) {
				return false
			}
		}
		position += kinds.size
		return true
	}
	
	fun match(predicate: (PzlToken) -> Boolean): Boolean {
		if (!predicate(current)) return false
		position++
		return true
	}
	
	fun check(kind: PzlTokenKind): Boolean {
		return current.kind == kind
	}
	
	fun check(check: (PzlToken) -> Boolean): Boolean {
		return check(current)
	}
	
	context(_: FileContext)
	fun expect(kind: PzlTokenKind, errorMessage: String) {
		if (current.kind == kind) {
			position++
			return
		}
		syntaxError(errorMessage, current)
	}
	
	fun isAtEnd(): Boolean {
		return position >= tokens.size || current.kind == EOF
	}
	
	operator fun get(index: Int): PzlToken {
		return tokens[index]
	}
}