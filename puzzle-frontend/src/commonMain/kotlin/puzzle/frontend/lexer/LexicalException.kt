package puzzle.frontend.lexer

import puzzle.core.exception.PzlException
import puzzle.core.exception.getExceptionMessage
import puzzle.core.context.FileContext
import puzzle.core.context.calcPosition

private class LexicalException(message: String) : PzlException(message)

context(file: FileContext)
fun lexicalError(message: String, position: Int): Nothing {
	val position = calcPosition(position)
	val message = getExceptionMessage("词法错误", message, file.path, position)
	throw LexicalException(message)
}