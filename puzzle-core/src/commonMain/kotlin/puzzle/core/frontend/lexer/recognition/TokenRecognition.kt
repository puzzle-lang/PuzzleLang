package puzzle.core.frontend.lexer.recognition

import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.token.PzlToken

sealed interface TokenRecognition {
	
	context(_: FileContext)
	fun tryParse(input: CharArray, start: Int): PzlToken?
}