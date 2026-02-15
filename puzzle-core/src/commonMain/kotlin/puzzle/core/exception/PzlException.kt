package puzzle.core.exception

import puzzle.core.io.FilePath
import puzzle.core.location.SourcePosition

abstract class PzlException(message: String) : RuntimeException(message)

fun getExceptionMessage(
    prefix: String,
    message: String,
    path: FilePath? = null,
    position: SourcePosition? = null,
): String {
    return buildString {
        append("[$prefix] ")
        append(message)
        append("\n位置: ")
        if (path != null) {
            append(path.absolutePath)
            if (position != null) {
                append(":${position.line}:${position.column}")
            }
        }
    }
}