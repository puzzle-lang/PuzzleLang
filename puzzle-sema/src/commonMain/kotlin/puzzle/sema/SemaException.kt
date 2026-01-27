package puzzle.sema

import puzzle.ast.PzlAstNode
import puzzle.core.exception.PzlException
import puzzle.core.exception.getExceptionMessage
import puzzle.core.location.SourceLocation
import puzzle.core.context.FileContext
import puzzle.core.context.startPosition

class SemaException(message: String) : PzlException(message)

context(file: FileContext)
fun semaError(message: String, node: PzlAstNode?): Nothing {
	val position = (node?.location as? SourceLocation.File)?.startPosition
	val message = getExceptionMessage("语法错误", message, file.path, position)
	throw SemaException(message)
}