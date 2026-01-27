package puzzle.frontend.lexer.recognition

import puzzle.core.context.FileContext
import puzzle.core.location.span
import puzzle.token.PzlToken
import puzzle.token.kinds.MetaKind.EOF

internal object EOFRecognition : TokenRecognition {
	
	context(_: FileContext)
	override fun tryParse(input: CharArray, start: Int): PzlToken? {
		if (start < input.size) return null
		return PzlToken(EOF, start span start + 1)
	}
}