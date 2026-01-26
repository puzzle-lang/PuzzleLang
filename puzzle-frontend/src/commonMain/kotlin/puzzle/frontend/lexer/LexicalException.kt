package puzzle.frontend.lexer

import puzzle.base.exception.PzlException
import puzzle.base.exception.getExceptionMessage
import puzzle.context.FileContext
import puzzle.context.calcPosition

private class LexicalException(message: String) : PzlException(message)

context(file: FileContext)
fun lexicalError(message: String, position: Int): Nothing {
	val position = calcPosition(position)
	val message = getExceptionMessage("词法错误", message, file.path, position)
	throw LexicalException(message)
}