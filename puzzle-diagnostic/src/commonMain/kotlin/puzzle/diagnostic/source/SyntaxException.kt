package puzzle.diagnostic.source

import puzzle.base.exception.PzlException
import puzzle.context.FileContext

class SyntaxException(message: String) : PzlException(message)

context(file: FileContext)
private fun syntaxError(
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