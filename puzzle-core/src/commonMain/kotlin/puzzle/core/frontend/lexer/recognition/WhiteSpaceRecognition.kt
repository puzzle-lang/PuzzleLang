package puzzle.core.frontend.lexer.recognition

import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.model.span
import puzzle.core.frontend.token.PzlToken
import puzzle.core.frontend.token.kinds.WhiteSpaceKind.WHITESPACE

object WhiteSpaceRecognition : TokenRecognition {
	
	context(_: FileContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		if (input[start] != ' ') return null
		var position = start + 1
		while (position < input.size && input[position] == ' ') {
			position++
		}
		return PzlToken(WHITESPACE, start span position)
	}
}