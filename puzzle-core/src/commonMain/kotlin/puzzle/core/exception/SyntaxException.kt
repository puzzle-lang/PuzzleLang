package puzzle.core.exception

import puzzle.core.frontend.ast.PzlAstNode
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.model.SourcePosition
import puzzle.core.frontend.model.calcPosition
import puzzle.core.frontend.token.PzlToken

class SyntaxException(message: String) : PzlException(message)

context(file: FileContext)
fun syntaxError(message: String, position: Int): Nothing {
	val position = calcPosition(position)
	syntaxError(message, position)
}

context(file: FileContext)
fun syntaxError(message: String, token: PzlToken): Nothing {
	val position = (token.location as? SourceLocation.File)?.startPosition
	syntaxError(message, position, token)
}

context(file: FileContext)
fun syntaxError(message: String, node: PzlAstNode?): Nothing {
	val position = (node?.location as? SourceLocation.File)?.startPosition
	syntaxError(message, position, null)
}

context(file: FileContext)
private fun syntaxError(
	message: String,
	position: SourcePosition?,
	token: PzlToken? = null,
): Nothing {
	val message = buildString {
		append("错误位置: ")
		append(file.sourcePath.absolutePath)
		position?.let { append(":${it.line}:${it.column}") }
		append(" ")
		token?.let { append(">> ${it.value} <<") }
		append(message)
	}
	throw SyntaxException(message)
}