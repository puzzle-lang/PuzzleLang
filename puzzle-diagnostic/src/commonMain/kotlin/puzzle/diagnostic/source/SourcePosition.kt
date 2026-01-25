package puzzle.diagnostic.source

import puzzle.context.FileContext

class SourcePosition(
	val line: Int,
	val column: Int,
)

context(file: FileContext)
fun calcPosition(position: Int): SourcePosition {
	val line = file.lineStarts.indexOfLast { position >= it }
	val column = position - file.lineStarts[line]
	return SourcePosition(line + 1, column + 1)
}