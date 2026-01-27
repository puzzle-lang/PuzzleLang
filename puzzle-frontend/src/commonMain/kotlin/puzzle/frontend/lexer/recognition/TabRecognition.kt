package puzzle.frontend.lexer.recognition

import puzzle.core.context.FileContext
import puzzle.core.location.span
import puzzle.token.PzlToken
import puzzle.token.kinds.WhiteSpaceKind.TAB

internal object TabRecognition : TokenRecognition {
	
	context(_: FileContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		if (input[start] != '\t') return null
		var position = start + 1
		while (position < input.size && input[position] == '\t') {
			position++
		}
		return PzlToken(TAB, start span position)
	}
}