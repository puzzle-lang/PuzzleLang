package puzzle.core.frontend.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import puzzle.core.frontend.token.PzlToken

@Serializable
sealed interface SourceLocation {
	
	val start: Int
	
	val end: Int
	
	@Serializable
	class File(
		override val start: Int,
		override val end: Int,
	) : SourceLocation {
		
		@Transient
		private var _startPosition: SourcePosition? = null
		
		@Transient
		private var _endPosition: SourcePosition? = null
		
		context(_: FileContext)
		val startPosition: SourcePosition
			get() = _startPosition ?: calcPosition(start).also { _startPosition = it }
		
		context(_: FileContext)
		val endPosition: SourcePosition
			get() = _endPosition ?: calcPosition(end).also { _endPosition = it }
	}
	
	@Serializable
	object Builtin : SourceLocation {
		override val start: Int
			get() = error("内置源位置")
		
		override val end: Int
			get() = error("内置源位置")
	}
	
	@Serializable
	object None : SourceLocation {
		override val start: Int
			get() = error("位置未知")
		
		override val end: Int
			get() = error("位置未知")
	}
}

context(_: FileContext)
fun PzlToken.equalsLine(token: PzlToken): Boolean {
	if (this.location !is SourceLocation.File || token.location !is SourceLocation.File) {
		return false
	}
	return this.location.endPosition.line == token.location.startPosition.line
}

@Suppress("NOTHING_TO_INLINE")
inline infix fun Int.span(end: Int): SourceLocation {
	return SourceLocation.File(this, end)
}

@Suppress("NOTHING_TO_INLINE")
inline infix fun SourceLocation.span(other: SourceLocation): SourceLocation {
	if (this !is SourceLocation.File || other !is SourceLocation.File) {
		return SourceLocation.Builtin
	}
	return SourceLocation.File(this.start, other.end)
}

fun SourceLocation.copy(
	start: (Int) -> Int = { it },
	end: (Int) -> Int = { it },
): SourceLocation {
	if (this !is SourceLocation.File) return SourceLocation.Builtin
	val start = start(this.start)
	val end = end(this.end)
	if (start == this.end && end == this.end) {
		return this
	}
	return SourceLocation.File(start, end)
}

context(file: FileContext)
fun calcPosition(position: Int): SourcePosition {
	val line = file.lineStarts.indexOfLast { position >= it }
	val column = position - file.lineStarts[line]
	return SourcePosition(line + 1, column + 1)
}

class SourcePosition(
	val line: Int,
	val column: Int,
)