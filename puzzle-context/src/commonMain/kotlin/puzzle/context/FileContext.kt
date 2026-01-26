package puzzle.context

import puzzle.base.io.FilePath
import puzzle.base.location.SourceLocation
import puzzle.base.location.SourcePosition

class FileContext : Context() {
	
	override lateinit var parent: ModuleContext
	
	var builtin = false
	
	lateinit var name: String
	
	lateinit var path: FilePath
	
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