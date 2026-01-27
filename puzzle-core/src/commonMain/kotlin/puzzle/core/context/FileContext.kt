package puzzle.core.context

import puzzle.core.io.FilePath
import puzzle.core.location.SourceLocation
import puzzle.core.location.SourcePosition

class FileContext : Context() {
	
	override lateinit var parent: ModuleContext
	
	var builtin = false
	
	lateinit var name: String
	
	var path: FilePath? = null
	
	lateinit var lineStarts: IntArray
}

context(_: FileContext)
val SourceLocation.startPosition: SourcePosition
	get() = calcPosition(this.start)

context(file: FileContext)
val SourceLocation.endPosition: SourcePosition
	get() = calcPosition(this.end)

context(file: FileContext)
fun calcPosition(position: Int): SourcePosition {
	val line = file.lineStarts.indexOfLast { position >= it }
	val column = position - file.lineStarts[line]
	return SourcePosition(line + 1, column + 1)
}