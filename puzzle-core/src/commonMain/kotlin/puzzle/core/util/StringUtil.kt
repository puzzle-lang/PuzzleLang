package puzzle.core.util

fun String.getIndentWidth(
	tabSize: Int = 4,
	ignoreFirstLine: Boolean = false,
): Int? {
	var min: Int? = null
	var col = 0
	var lineIndex = 0
	var i = 0
	val n = length
	
	while (i <= n) {
		val c = if (i < n) this[i] else '\n'
		
		if (c == '\n') {
			if (!(ignoreFirstLine && lineIndex == 0) && col > 0) {
				min = if (min == null) col else minOf(min, col)
			}
			lineIndex++
			col = 0
			i++
			continue
		}
		
		if (lineIndex == 0 && ignoreFirstLine) {
			i++
			continue
		}
		
		col += when (c) {
			' ' -> 1
			'\t' -> tabSize - (col % tabSize)
			else -> {
				i++
				while (i < n && this[i] != '\n') i++
				continue
			}
		}
		i++
	}
	
	return min
}

fun String.removeIndent(
	indentWidth: Int,
	tabSize: Int = 4,
	ignoreFirstLine: Boolean = false,
): String {
	val sb = StringBuilder(length)
	var col = 0
	var row = 0
	for (char in this) {
		if (char == '\n') {
			col = 0
			row++
			sb.append(char)
			continue
		}
		if (ignoreFirstLine && row == 0 || col >= indentWidth) {
			sb.append(char)
			continue
		}
		when (char) {
			' ' -> col++
			'\t' -> col += tabSize - (col % tabSize)
			else -> error("改字符串不支持移除长度为: $indentWidth 的公共前缀")
		}
	}
	return sb.toString()
}