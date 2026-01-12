package puzzle.core.frontend.parser

import puzzle.core.frontend.ast.AstFile
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.parser.parseFile

object PzlParser {
	
	context(context: FileContext)
	fun parse(): AstFile {
		return context(PzlTokenCursor(context.tokens)) {
			parseFile()
		}
	}
}