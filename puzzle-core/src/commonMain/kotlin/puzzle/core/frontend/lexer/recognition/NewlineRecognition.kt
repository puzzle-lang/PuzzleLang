package puzzle.core.frontend.lexer.recognition

import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.span
import puzzle.core.frontend.token.PzlToken
import puzzle.core.frontend.token.kinds.WhiteSpaceKind.NEWLINE

object NewlineRecognition : TokenRecognition {
	
	context(_: FileContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		val char = input[start]
		if (char != '\n') return null
		return PzlToken(NEWLINE, start span start + 1)
	}
}