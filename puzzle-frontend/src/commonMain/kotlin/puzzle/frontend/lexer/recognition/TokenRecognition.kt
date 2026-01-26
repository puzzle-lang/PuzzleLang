package puzzle.frontend.lexer.recognition

import puzzle.context.FileContext
import puzzle.token.PzlToken

internal sealed interface TokenRecognition {
	
	context(_: FileContext)
	fun tryParse(input: CharArray, start: Int): PzlToken?
}