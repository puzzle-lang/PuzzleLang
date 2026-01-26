package puzzle.base.location

import kotlinx.serialization.Serializable

@Serializable
sealed interface SourceLocation {
	
	val start: Int
	
	val end: Int
	
	@Serializable
	class File(
		override val start: Int,
		override val end: Int,
	) : SourceLocation
	
	@Serializable
	object Builtin : SourceLocation {
		
		override val start: Int
			get() = error("内置源位置")
		
		override val end: Int
			get() = error("内置源位置")
	}
}

infix fun Int.span(end: Int): SourceLocation {
	return SourceLocation.File(this, end)
}

infix fun SourceLocation.span(other: SourceLocation): SourceLocation {
	if (this !is SourceLocation.File || other !is SourceLocation.File) {
		return SourceLocation.Builtin
	}
	if (this.start >= other.end) error("this.start >= other.end")
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