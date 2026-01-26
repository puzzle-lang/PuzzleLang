package puzzle.frontend.parser

import puzzle.ast.AstFile
import puzzle.context.FileContext
import puzzle.frontend.parser.parser.parseFile
import puzzle.token.TokenAttachment

object PzlParser {
	
	context(file: FileContext)
	fun parse(): AstFile {
		val tokens = file[TokenAttachment::class].value
		val cursor = PzlTokenCursor(tokens)
		return context(cursor) { parseFile() }
	}
}