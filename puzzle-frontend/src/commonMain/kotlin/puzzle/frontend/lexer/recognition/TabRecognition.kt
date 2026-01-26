package puzzle.frontend.lexer.recognition

import puzzle.context.FileContext
import puzzle.base.location.span
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