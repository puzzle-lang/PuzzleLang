package puzzle.frontend.parser

import puzzle.ast.PzlAstNode
import puzzle.core.context.FileContext
import puzzle.core.context.calcPosition
import puzzle.core.context.startPosition
import puzzle.core.exception.PzlException
import puzzle.core.exception.getExceptionMessage
import puzzle.core.location.SourceLocation
import puzzle.core.location.SourcePosition
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


context(file: FileContext)
fun syntaxError(message: String, position: SourcePosition?): Nothing {
	val message = getExceptionMessage("语法错误", message, file.path, position)
	throw SyntaxException(message)
}

context(file: FileContext)
fun syntaxError(message: String, position: Int): Nothing {
	val position = calcPosition(position)
	val message = getExceptionMessage("语法错误", message, file.path, position)
	throw SyntaxException(message)
}