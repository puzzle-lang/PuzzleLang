package puzzle.base.exception

import puzzle.base.io.FilePath
import puzzle.base.location.SourcePosition

abstract class PzlException(message: String) : RuntimeException(message)

fun getExceptionMessage(
	prefix: String,
	message: String,
	path: FilePath? = null,
	position: SourcePosition? = null,
): String {
	return buildString {
		append("[$prefix] ")
		appendLine(message)
		if (path != null) {
			append(path.absolutePath)
			if (position != null) {
				append(":${position.line}:${position.column}")
			}
		}
	}
}