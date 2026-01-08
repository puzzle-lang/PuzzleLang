package puzzle.core.exception

import puzzle.core.frontend.ast.AstNode
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.model.SourcePosition
import puzzle.core.frontend.model.calcPosition
import puzzle.core.frontend.token.PzlToken
import puzzle.core.util.PathWrapper

private class SyntaxException(message: String) : Exception(message)

context(context: FileContext)
fun syntaxError(message: String, position: Int): Nothing {
	val position = calcPosition(position)
	val message = buildSyntaxExceptionMessage(message, context.sourcePath, position, null)
	throw SyntaxException(message)
}

context(context: FileContext)
fun syntaxError(message: String, token: PzlToken): Nothing {
	val position = (token.location as? SourceLocation.File)?.startPosition
	val message = buildSyntaxExceptionMessage(message, context.sourcePath, position, token)
	throw SyntaxException(message)
}

context(context: FileContext)
fun syntaxError(message: String, node: AstNode): Nothing {
	val position = (node.location as? SourceLocation.File)?.startPosition
	val message = buildSyntaxExceptionMessage(message, context.sourcePath, position, null)
	throw SyntaxException(message)
}

private fun buildSyntaxExceptionMessage(
	message: String,
	sourcePath: PathWrapper,
	position: SourcePosition?,
	token: PzlToken?,
): String = buildString {
	append("错误位置: ")
	append(sourcePath.absolutePath)
	if (position != null) {
		this.append(":${position.line}:${position.column}")
	}
	append(" ")
	if (token != null) {
		append(">> ${token.value} << ")
	}
	append(message)
}