package puzzle.frontend.lexer.recognition

import puzzle.context.FileContext
import puzzle.base.location.span
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