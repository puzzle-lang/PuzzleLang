package puzzle.core.frontend.parser

import puzzle.core.frontend.ast.AstFile
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.parser.parseFile

object PzlParser {
	
	context(file: FileContext)
	fun parse(): AstFile {
		return context(PzlTokenCursor(file.tokens)) {
			parseFile()
		}
	}
}