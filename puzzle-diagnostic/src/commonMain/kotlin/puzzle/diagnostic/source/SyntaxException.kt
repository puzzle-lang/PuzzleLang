package puzzle.diagnostic.source

import puzzle.base.exception.PzlException
import puzzle.base.location.SourcePosition
import puzzle.context.FileContext

private class SyntaxException(message: String) : PzlException(message)

fun syntaxError(message: String): Nothing {
	throw SyntaxException(message)
}

context(file: FileContext)
fun syntaxError(
	message: String,
	position: SourcePosition?,
): Nothing {
	val message = buildString {
		appendLine(message)
		append("错误位置: ")
		append(file.path.absolutePath)
		if (position != null) {
			append(":${position.line}:${position.column}")
		}
	}
	throw SyntaxException(message)
}