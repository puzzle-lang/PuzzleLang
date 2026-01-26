package puzzle.frontend.parser

import puzzle.ast.PzlAstNode
import puzzle.base.exception.PzlException
import puzzle.base.exception.getExceptionMessage
import puzzle.base.location.SourceLocation
import puzzle.context.FileContext
import puzzle.context.startPosition
import puzzle.token.PzlToken

private class SyntaxException(message: String) : PzlException(message)

context(file: FileContext)
fun syntaxError(message: String, token: PzlToken): Nothing {
	val position = (token.location as? SourceLocation.File)?.startPosition
	val message = getExceptionMessage("语法错误", message, file.path, position)
	throw SyntaxException(message)
}

context(file: FileContext)
fun syntaxError(message: String, node: PzlAstNode): Nothing {
	val position = (node.location as? SourceLocation.File)?.startPosition
	val message = getExceptionMessage("语法错误", message, file.path, position)
	throw SyntaxException(message)
}