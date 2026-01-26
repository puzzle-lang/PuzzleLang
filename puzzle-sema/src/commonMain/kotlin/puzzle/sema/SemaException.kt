package puzzle.sema

import puzzle.ast.PzlAstNode
import puzzle.base.exception.PzlException
import puzzle.base.exception.getExceptionMessage
import puzzle.base.location.SourceLocation
import puzzle.context.FileContext
import puzzle.context.startPosition

class SemaException(message: String) : PzlException(message)

context(file: FileContext)
fun semaError(message: String, node: PzlAstNode?): Nothing {
	val position = (node?.location as? SourceLocation.File)?.startPosition
	val message = getExceptionMessage("语法错误", message, file.path, position)
	throw SemaException(message)
}