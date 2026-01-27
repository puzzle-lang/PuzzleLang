package puzzle.frontend.lexer.recognition

import puzzle.core.context.FileContext
import puzzle.core.location.span
import puzzle.token.PzlToken
import puzzle.token.kinds.WhiteSpaceKind.NEWLINE

internal object NewlineRecognition : TokenRecognition {
	
	context(_: FileContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		val char = input[start]
		if (char != '\n') return null
		return PzlToken(NEWLINE, start span start + 1)
	}
}